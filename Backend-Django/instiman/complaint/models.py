from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import pre_save, post_save, pre_delete
from django.dispatch import receiver
from gcm.models import AbstractDevice
from rest_framework.authtoken.models import Token

# for Gcm
class MyDevice(AbstractDevice):
	user = models.ForeignKey(User,related_name='user_device')

@receiver(pre_save, sender=MyDevice)
def assign_user(sender, instance, *args, **kwargs):
    user = Token.objects.get(key=instance.name)
    instance.user = user.user



# Create your models here.

class Category(models.Model):
	name = models.CharField(max_length=20)

	def __str__(self):
		return self.name

class ManagementUser(models.Model):
	user = models.ForeignKey(User)
	category = models.ForeignKey(Category)
	def __str__(self):
		return self.user.username



class Worker(models.Model):
	user = models.ForeignKey(User, related_name='worker')
	category = models.ForeignKey(Category, related_name='workers_category')
	
	def __str__(self):
		return self.user.username


class WorkerList(models.Model):
	
	name = models.CharField(max_length=120)
	number = models.CharField(max_length=120)
	category = models.ForeignKey(Category,  blank=True, null=True)
	
	def __str__(self):
		return self.name


class ManagerList(models.Model):
	
	name = models.CharField(max_length=120)
	email = models.EmailField()
	category = models.ForeignKey(Category, blank=True, null=True)
	# number = models.CharField(max_length=120)

	
	def __str__(self):
		return self.name


class Complaint(models.Model):
	user = models.ForeignKey(User, null=True, blank=True)
	# slug = models.SlugField(unique=True)
	assigned_by = models.ForeignKey(User, related_name="assigned_by", null=True, blank=True)
	assigned_to = models.ForeignKey(User, related_name="assigned_to", null=True, blank=True)
	title = models.CharField(max_length=120)
	image = models.FileField(blank=True,null=True)
	category =models.ForeignKey(Category, blank=True, null=True)
	location = models.CharField(max_length=255)
	description = models.TextField()
	timestamp = models.DateTimeField(auto_now = False, auto_now_add=True)
	updated = models.DateTimeField(blank = True,null=True,auto_now = True, auto_now_add=False)
	number = models.BigIntegerField(blank=True,null=True)
	status = models.CharField(max_length=20, default = 'Submitted')
	comment = models.TextField(blank=True)
	priority = models.IntegerField(default=0)


	def __str__(self):
		return ("%s:%s") %(self.user, self.title)

	# def get_absolute_url(self):
 #        return reverse("complaint:detail", kwargs={"slug": self.slug})



class Log(models.Model):
	complaint = models.ForeignKey(Complaint)
	user = models.ForeignKey(User, blank=True, null=True)
	time = models.DateTimeField(auto_now=True, auto_now_add=False)
	status = models.CharField(max_length=120)
	comment = models.TextField(blank=True)
	assigned_to =models.ForeignKey(User,blank=True, null=True, related_name='assigned_to_log')
	def __str__(self):
		return self.complaint.title






## To update the log
from .serializers import ComplaintSerializer
@receiver(post_save, sender=User)
def create_managment(sender, instance=None, created=False, **kwargs):
	if ManagerList.objects.filter(email=instance.email).exists():
		ManagementUser.objects.get_or_create(user=instance,category=ManagerList.objects.get(email=instance.email).category)

@receiver(post_save, sender=Complaint)
def update_status(sender, instance=None, created=False, **kwargs):
	if instance.assigned_by != None:
		management_users = ManagementUser.objects.filter(category = instance.category).values('user__id')
		log = Log.objects.create(user=instance.assigned_by, assigned_to=instance.assigned_to, complaint=instance, status = instance.status, comment = instance.comment)
		try:
			my_phone = MyDevice.objects.filter(user= instance.user)
			complaint = Complaint.objects.get(id=instance.id)
			serializer = ComplaintSerializer(complaint, many=False)
			my_phone.send_message({"complaint": serializer.data}, collapse_key='something')
			MyDevice.objects.filter(user__id__in=management_users).send_message({"complaint": serializer.data}, collapse_key='something')
			MyDevice.objects.filter(user=instance.assigned_to).send_message({"complaint": serializer.data}, collapse_key='something')
			
		except MyDevice.DoesNotExist:
			pass
	else:
		management_users = ManagementUser.objects.filter(category = instance.category).values('user__id')
		log = Log.objects.create(user=instance.assigned_by, assigned_to=instance.assigned_to, complaint=instance, status = instance.status, comment = instance.comment)
		try:
			my_phone = MyDevice.objects.filter(user= instance.user)
			complaint = Complaint.objects.get(id=instance.id)
			serializer = ComplaintSerializer(complaint, many=False)
			my_phone.send_message({"complaint": serializer.data}, collapse_key='something')
			MyDevice.objects.filter(user__id__in=management_users).send_message({"complaint": serializer.data}, collapse_key='something')
		except MyDevice.DoesNotExist:
			pass

from django.db import models
from django.contrib.auth.models import User
from complaint.models import Complaint, Category
from rest_framework.authtoken.models import Token
from django.db.models.signals import pre_save, post_save, pre_delete
from django.dispatch import receiver
from django.http import HttpResponse
from gcm.models import AbstractDevice
from .devices import MyDevice

# Create your models here.



class ManagementUser(models.Model):
	user = models.ForeignKey(User)
	category = models.ForeignKey(Category)
	def __str__(self):
		return self.user.username



class Worker(models.Model):
	user = models.ForeignKey(User, related_name='worker')
	def __str__(self):
		return self.user.username






@receiver(pre_save, sender=MyDevice)
def assign_user(sender, instance, *args, **kwargs):
    user = Token.objects.get(key=instance.name)
    instance.user = user.user

class Management(models.Model):
	user = models.ForeignKey(User, related_name='management')
	changed_status = models.CharField(max_length = 120, default='Submitted')
	timestamp = models.DateTimeField(auto_now=False, auto_now_add=True)
	assigned_to = models.ForeignKey(User, related_name='massigned_to')
	complaint = models.ForeignKey(Complaint, related_name='complaint')
	priority = models.IntegerField(default=0)
	attended = models.IntegerField(default=0)
	comment = models.TextField(blank=True)
	def __str__(self):
		return self.user.username

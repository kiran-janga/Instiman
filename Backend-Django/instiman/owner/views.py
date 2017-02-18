
# try:
#     from urllib import quote_plus #python 2
# except:
#     pass

# try:
#     from urllib.parse import quote_plus #python 3
# except:
#     pass
# Create your views here.
from django.contrib import messages
from complaint.models import Complaint,Worker,Category,ManagementUser,WorkerList,ManagerList
from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger
from django.db.models import Q
from django.template.context_processors import csrf
from django.contrib.auth import authenticate, login, logout
from django.http import HttpResponse, HttpResponseRedirect, Http404
from django.shortcuts import render, get_object_or_404, redirect
from django.utils import timezone
from .forms import ComplaintForm,WorkerForm,ManagerForm,CategoryForm,UserCreateForm, LogInForm
from django.contrib.auth.models import User
from django.core.serializers.json import DjangoJSONEncoder
from django.contrib.auth.decorators import login_required
import json
from django.contrib.auth.forms import PasswordChangeForm
from django.contrib.auth import update_session_auth_hash
@login_required
def change_password(request):
	if request.method == 'POST':
		form = PasswordChangeForm(request.user, data=request.POST)
		if form.is_valid():
			form.save()
			update_session_auth_hash(request, form.user) # dont logout the user.
			messages.success(request, "Password changed.")
			return redirect("/owner/")
	else:
		form = PasswordChangeForm(request.user)
	context = {
		'form':form,
	}
	return render(request,"change_password.html", context)


def complaint_list(request):
	today = timezone.now().date()
	queryset_list = Complaint.objects.all().order_by("-timestamp")

	if request.user.is_staff or request.user.is_superuser:
		queryset_list = Complaint.objects.all()

	query = request.GET.get("q")

	if query:
		queryset_list = queryset_list.filter(
				Q(title__icontains=query)
				).distinct()

	query2 = request.GET.get("category")
	if query2:
		queryset_list = queryset_list.filter(
				Q(category__name__icontains=query2)
				).distinct()
	# paginator = Paginator(queryset_list, 8) # Show 25 contacts per page
	# page_request_var = "page"
	# page = request.GET.get(page_request_var)
	# try:
	# 	queryset = paginator.page(page)
	# except PageNotAnInteger:
	# 	# If page is not an integer, deliver first page.
	# 	queryset = paginator.page(1)
	# except EmptyPage:
	# 	# If page is out of range (e.g. 9999), deliver last page of results.
	# 	queryset = paginator.page(paginator.num_pages)


	context = {
		"object_list": queryset_list,
		"title": "",
		# "page_request_var": page_request_var,
		"today": today,
	}
	return render(request, "complaint_list.html", context)



def complaint_detail(request, complaint_id=None):
	instance = Complaint.objects.get(id = complaint_id)
	# if instance.publish > timezone.now().date() or instance.draft:
	# 	if not request.user.is_staff or not request.user.is_superuser:
	# 		raise Http404
	# share_string = quote_plus(instance.content)
	context = {
		"title": instance.title,
		"instance": instance,
		# "share_string": share_string,
	}
	return render(request, "complaint_detail.html", context)


def complaint_update(request, complaint_id=None):

	instance = Complaint.objects.get(id = complaint_id)

	print (instance)

	form = ComplaintForm(request.POST or None, request.FILES or None, instance=instance)

	if form.is_valid():
		instance = form.save(commit=False)
		instance.save()
		messages.success(request, "<a href='#'>Item</a> Saved", extra_tags='html_safe')
		return HttpResponseRedirect("/owner/complaints/"+str(complaint_id)+"/")

	context = {
		"title": instance.title,
		"instance": instance,
		"form":form,
	}
	return render(request, "complaint_form.html", context)


def add_worker(request):

	form  = WorkerForm(request.POST or None)

	if form.is_valid():

		form_name = form.cleaned_data.get("name")
		form_number = form.cleaned_data.get("number")
		form_category = form.cleaned_data.get("category")
		instance = WorkerList.objects.create()
		worker = User.objects.create(username=form_number, password=form_number, first_name=form_name)
		Worker.objects.create(user = worker, category = form_category )
		instance.name = form_name
		instance.number = form_number
		instance.category = form_category
		instance.save()
		return HttpResponseRedirect("/owner/categories/"+str(form_category.id)+"/")

	context = {
	    "title":"Add Worker",
		"form":form,
	}

	return render(request, "complaint_form.html", context)

def edit_worker(request, worker_id=None):

	instance2 = WorkerList.objects.get(id = worker_id)
	try:
		instance3 = User.objects.get(first_name = instance2.name)
		exists = True
	except:
		exists = False

	form = WorkerForm(request.POST or None, request.FILES or None, instance=instance2)

	if form.is_valid():

		if request.POST.get("edit"):
			form_name = form.cleaned_data.get("name")
			form_number = form.cleaned_data.get("number")
			form_category = form.cleaned_data.get("category")

			instance2.name = form_name
			instance2.number = form_number
			instance2.category = form_category
			instance2.save()

			if exists:
				instance3.first_name = form_name
				instance3.username = form_number
				instance3.save()
				instance1 = Worker.objects.get(user = instance3)
				instance1.category = form_category
				instance1.save()
			return HttpResponseRedirect("/owner/categories/"+str(form_category.id)+"/")

		elif request.POST.get("delete"):

			instance2.delete()
			instance1 = Worker.objects.get(user = instance3)
			instance1.delete()
			instance3.delete()
			return HttpResponseRedirect("/owner/categories/"+str(form_category.id)+"/")

	context = {
		"title": "Edit Worker",
		"form":form,
	}
	return render(request, "complaint_form.html", context)



def worker_list(request,category_id = None):
	queryset_list_worker = WorkerList.objects.all()
	queryset_list_manager = ManagerList.objects.all()


	queryset_list_worker = queryset_list_worker.filter(category__id=category_id)


	queryset_list_manager= queryset_list_manager.filter(category__id=category_id)

	query_worker = request.GET.get("worker")
	query_manager = request.GET.get("manager")

	if query_manager:
		queryset_list_manager = queryset_list_manager.filter(
				Q(user__em__icontains=query_manager)
				).distinct()

	if query_worker:
		queryset_list_worker = queryset_list_worker.filter(
				Q(user__first_name__icontains=query_worker)
				).distinct()

	context = {
		"worker_list": queryset_list_worker,
		"manager_list": queryset_list_manager,
		"title": Category.objects.get(id=category_id).name,
		"categoryId": category_id,
		# "page_request_var": page_request_var,

	}
	return render(request, "worker_list.html", context)


def add_manager(request):

	form  = ManagerForm(request.POST or None)

	if form.is_valid():

		form_name = form.cleaned_data.get("name")
		form_email = form.cleaned_data.get("email")
		form_category = form.cleaned_data.get("category")
		instance = ManagerList.objects.create()
		instance.name = form_name
		instance.email= form_email
		instance.category = form_category
		instance.save()
		return HttpResponseRedirect("/owner/categories/"+str(form_category.id)+"/")

	context = {

	    "title":"Add Manager",
		"form":form,
	}

	return render(request, "complaint_form.html", context)

def edit_manager(request, manager_id=None):

	instance2 = ManagerList.objects.get(id = manager_id)

	try:
		instance3 = User.objects.get(username = instance2.name)
		exists = True
	except :
		exists = False


	form = ManagerForm(request.POST or None, request.FILES or None, instance=instance2)

	if form.is_valid():

		if request.POST.get("edit"):
			form_name = form.cleaned_data.get("name")
			form_email = form.cleaned_data.get("email")
			form_category = form.cleaned_data.get("category")

			instance2.name = form_name
			instance2.email = form_email
			instance2.category = form_category
			instance2.save()

			if exists:
				instance3.username = form_name
				instance3.email = form_email
				instance3.save()
				instance1 = ManagementUser.objects.get(user = instance3)
				instance1.category = form_category
				instance1.save()

			return HttpResponseRedirect("/owner/categories/"+str(form_category.id)+"/")

		elif request.POST.get("delete"):

			instance2.delete()

			if exists:
				instance1 = ManagementUser.objects.get(user = instance3)
				instance1.delete()
				instance3.delete()
		return HttpResponseRedirect("/owner/categories/"+str(form_category.id)+"/")

	context = {
		"title": "Edit Manager",
		"form":form,
	}
	return render(request, "complaint_form.html", context)

def add_category(request):

	form  = CategoryForm(request.POST or None)

	if form.is_valid():

		form_name = form.cleaned_data.get("name")
		instance = Category.objects.create()
		instance.name = form_name
		instance.save()
		return HttpResponseRedirect("/owner/")

	context = {

	    "title":"Add Category",
		"form":form,
	}

	return render(request, "complaint_form.html", context)

@login_required
def owner(request):

	dict1 = {}
	category_number = len(Category.objects.all())

	print (category_number)

	complaintNumber = len(Complaint.objects.all())
	print (complaintNumber)
	queryset_list = Category.objects.all()

	for i in range (0,category_number):

		queryset_category_list = Complaint.objects.filter(category = queryset_list[i])

		dict1[queryset_list[i].id] = {"category_name":str(queryset_list[i].name),"total":len(queryset_category_list.filter(status__in = ["Submitted","In Process","pending"])),"new":len(queryset_category_list.filter(status = "Submitted")),"inProcess":len(queryset_category_list.filter(status = "In Process")),"pending":len(queryset_category_list.filter(status = "pending")),"rejected":len(queryset_category_list.filter(status = "rejected")),"similar":len(queryset_category_list.filter(status = "similar"))}

	paginator = Paginator(queryset_list, 10) # Show 25 contacts per page
	page_request_var = "page"
	page = request.GET.get(page_request_var)
	try:
		queryset = paginator.page(page)
	except PageNotAnInteger:
		# If page is not an integer, deliver first page.
		queryset = paginator.page(1)
	except EmptyPage:
		# If page is out of range (e.g. 9999), deliver last page of results.
		queryset = paginator.page(paginator.num_pages)
	# dict1 = json.dumps(dict1, cls=DjangoJSONEncoder)
	context = {
	    "complaint_number":complaintNumber,
	    "category_list": queryset,
	    "dict" : dict1 ,
		"title": "Categories",
		"page_request_var": page_request_var,
	}
	return render(request, "owner_main.html", context)


def LogIn(request):
	form = LogInForm(request.POST or None)

	if request.method == "POST":
		username = request.POST.get('username')
		password = request.POST.get('password')
		user = authenticate(username=username, password=password)

		if user is not None:
			if user.is_active:
				login(request, user)

				return HttpResponseRedirect("/owner/")

		else:
			messages.error(request, "Incorect Username or Password")

	context = {
		'form':form,
	}

	context.update(csrf(request))
	return render(request, "login.html", context)

def Logout(request):
	logout(request)
	return HttpResponseRedirect("/")


def add_owner(request):

	form  = UserCreateForm(request.POST or None)

	if form.is_valid():

		form_name = form.cleaned_data.get("name")

		form_name = form.cleaned_data.get("name")
		form_email = form.cleaned_data.get("email")
		form_category = form.cleaned_data.get("category")


		instance = Category.objects.create()
		instance.name = form_name
		instance.save()

	context = {

	    "title":"Add Owner",
		"form":form,
	}

	return render(request, "complaint_form.html", context)


def delete_category(request, category_id=None):

	instance = Category.objects.get(id = category_id)

	print (len(WorkerList.objects.filter(category = instance)))
	print (len(ManagerList.objects.filter(category = instance)))
	print (len(Complaint.objects.filter(category = instance)))

	if len(WorkerList.objects.filter(category = instance))==0 and len(ManagerList.objects.filter(category = instance))==0 and len(Complaint.objects.filter(category = instance))==0:
		instance.delete()
		return HttpResponseRedirect("/owner/")

	return "Cannot Delete"

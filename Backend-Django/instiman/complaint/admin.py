from django.contrib import admin
from .models import Complaint, Category, Log, Worker, ManagementUser,WorkerList,ManagerList
# Register your models here.
admin.site.register(Complaint)
admin.site.register(Category)
admin.site.register(Log)
admin.site.register(Worker)
admin.site.register(ManagementUser)
admin.site.register(WorkerList)
admin.site.register(ManagerList)

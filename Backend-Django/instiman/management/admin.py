from django.contrib import admin
from .models import Management, ManagementUser, Worker
# Register your models here.
admin.site.register(Management)
admin.site.register(ManagementUser)
admin.site.register(Worker)

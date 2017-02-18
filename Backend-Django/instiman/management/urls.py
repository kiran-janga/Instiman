from django.conf.urls import url, include
from .views import ManagementView, AssignView, WorkerListView

urlpatterns=[
	url(r'^assign/$', AssignView.as_view(), name='assign'),
	url(r'^worker/$', WorkerListView.as_view(), name='assign'),
	

]

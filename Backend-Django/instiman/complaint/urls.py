from django.conf.urls import url, include
from .views import ComplaintView, CategoryView, DescriptionView, ComplaintAssignView, ManagementView, WorkerListView, WorkerComplaintView, CreateWorker

urlpatterns=[
	url(r'^$', ComplaintView.as_view(), name='complaint'),
	url(r'^assign/(?P<complaint_id>[0-9]+)/$', ComplaintAssignView.as_view(), name='assign1'),
	url(r'^category/$', CategoryView.as_view(), name='category'),
	url(r'^(?P<complaint_id>[0-9]+)/$', DescriptionView.as_view(), name='description'),
	url(r'^management/$', ManagementView.as_view(), name='management'),
	url(r'^workers/$', WorkerListView.as_view(), name='workers'),
	url(r'^worker_complaint/$', WorkerComplaintView.as_view(), name='workers_complaints'),
	url(r'^create_worker/$', CreateWorker.as_view(), name='create_worker'),
]

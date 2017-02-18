from django.conf.urls import url, include

from .views import (
	owner,
	complaint_list,
	worker_list,
	add_worker,
	complaint_detail,
	complaint_update,
	# post_delete,
	edit_worker,
	add_manager,
	edit_manager,
	add_category,
	Logout,
	add_owner,
	delete_category,
	change_password,
	)

urlpatterns=[

    url(r'^$', owner, name='owner'),
	url(r'^change_password/$', change_password, name ='change_password'),
	url(r'^complaints/$', complaint_list, name='complaintList'),
	url(r'^complaints/(?P<complaint_id>[0-9]+)/$', complaint_detail, name='complaintDetail'),
	url(r'^complaints/(?P<complaint_id>[0-9]+)/edit/$', complaint_update, name='complaintUpdate'),

	url(r'^worker/add/$', add_worker, name='addWorker'),
	url(r'^worker/(?P<worker_id>[0-9]+)/edit/$', edit_worker, name='editWorker'),

	url(r'^manager/add/$', add_manager, name='addWorker'),
	url(r'^manager/(?P<manager_id>[0-9]+)/edit/$', edit_manager, name='editManager'),
	# url(r'^worker/delete/$', delete_worker, name='addWorker'),
	# # url(r'^worker/$', WorkerListView.as_view(), name='assign'),

	url(r'^categories/add/$', add_category, name='addCategory'),
	url(r'^categories/(?P<category_id>[0-9]+)/delete/$', delete_category, name='deleteCategory'),
	url(r'^categories/(?P<category_id>[0-9]+)/$', worker_list, name='workerList'),

	url(r'^Logout/$', Logout, name='ownerLogout'),
	url(r'^add/$', add_owner, name='addOwner'),
]

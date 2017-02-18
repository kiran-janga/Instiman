from gcm.models import AbstractDevice
from django.contrib.auth.models import User
from django.db import models
# for Gcm
class MyDevice(AbstractDevice):
	user = models.ForeignKey(User,related_name='user_device')

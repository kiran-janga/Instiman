from complaint.models import Complaint
from .models import ManagementUser, Management, Worker
from rest_framework import serializers
from django.contrib.auth.models import User
from complaint.serializers import ComplaintSerializer

class UserSerializer(serializers.ModelSerializer):
	class Meta:
		model = User
		fields = ('username', 'id')

class ManagementSerializer(serializers.ModelSerializer):
	user = serializers.SlugRelatedField(read_only=True, slug_field='email')
	class Meta:
		model = Complaint
		fields = ('user', 'id', 'image','category', 'location', 'description', 'status', 'timestamp', 'comment')

class ComplaintAssignSerializer(serializers.ModelSerializer):
	class Meta:
		model = Management
		fields = ('assigned_to','complaint','timestamp','changed_status', 'comment', 'attended', 'priority')


class WorkerSerializer(serializers.ModelSerializer):
	user = UserSerializer()
	class Meta:
		model = Worker
		fields = ('user',)

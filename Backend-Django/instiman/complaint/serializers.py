from .models import Complaint, Category, Worker
from rest_framework import serializers
from django.contrib.auth.models import User


class UserSerializer(serializers.ModelSerializer):
	class Meta:
		model = User
		fields = ('username', 'id','first_name')


class ComplaintSerializer(serializers.ModelSerializer):
	user = serializers.SlugRelatedField(read_only=True, slug_field='email')
	assigned_to = serializers.SlugRelatedField(read_only=True, slug_field='first_name')
	assigned_by = serializers.SlugRelatedField(read_only=True, slug_field='email')
	class Meta:
		model = Complaint
		fields = ('id','user', 'image','category', 'location', 'description', 'status', 'timestamp', 'comment', 'title', 'priority', 'assigned_to', 'assigned_by')


class CategorySerializer(serializers.ModelSerializer):
	class Meta:
		model = Category
		fields = ('name','id')

class ComplaintAssignSerializer(serializers.ModelSerializer):
	class Meta:
		model = Complaint
		fields = ('assigned_to','status', 'comment', 'priority', "category")

class WorkerSerializer(serializers.ModelSerializer):
	user = UserSerializer()
	class Meta:
		model = Worker
		fields = ('user', 'id', 'category')

class CreateWorkerSerializer(serializers.ModelSerializer):
	class Meta:
		model = User
		fields = ('username', 'password','first_name')

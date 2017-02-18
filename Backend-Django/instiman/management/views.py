from django.shortcuts import render
from rest_framework import generics
from rest_framework.views import APIView
from django.contrib.auth.models import User
from rest_framework import routers, serializers, viewsets
from django.http import Http404
from rest_framework.response import Response
from rest_framework import status
from .serializers import ManagementSerializer, ComplaintAssignSerializer, WorkerSerializer
from django.core.mail import send_mail
from django.conf import settings
from complaint.models import Complaint
from .models import Worker, ManagementUser
from complaint.serializers import ComplaintSerializer
# Create your views here.




class AssignView(APIView):

    def post(self, request, format=None):
        serializer = ComplaintAssignSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save(user=request.user)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)



class WorkerListView(APIView):

	def get(self, request, format=None):
		query_set = Worker.objects.all()
		serializer = WorkerSerializer(query_set, many=True)
		return Response(serializer.data)

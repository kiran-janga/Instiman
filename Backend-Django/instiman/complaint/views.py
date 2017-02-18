from django.shortcuts import render
from rest_framework import generics
from rest_framework.views import APIView
from django.contrib.auth.models import User
from rest_framework import routers, serializers, viewsets
from django.http import Http404
from rest_framework.response import Response
from rest_framework import status
from .serializers import ComplaintSerializer, CategorySerializer, ComplaintAssignSerializer, WorkerSerializer, UserSerializer, CreateWorkerSerializer
from django.core.mail import send_mail
from django.conf import settings
from .models import Complaint, Category, ManagementUser, Worker
from django.conf import settings

from rest_framework import generics

# Create your views here.
class ComplaintView(APIView):


    def get(self, request, format=None):
        query_set = Complaint.objects.filter(user = request.user) ##[request.GET.get('offset'):request.GET.get('offset')+request.GET.get('limit')]

        serializer = ComplaintSerializer(query_set, many = True)

        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = ComplaintSerializer(data=request.data)

        if serializer.is_valid():
            serializer.save(user=request.user)

            # ##Mail
            # subject = "New Complaint from InstiMan"
            # from_email = settings.EMAIL_HOST_USER
            # msg = "image: "+"http://10.0.25.180:80"+serializer.data['image']+"\n"+"Description: " + serializer.data['description']+"\n"+"Location: "+ serializer.data['location']
            # send_mail(subject,msg,from_email,['sriraghu_malireddi@iitgn.ac.in',], fail_silently=False)

            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class ComplaintAssignView(APIView):
    def get_object(self, complaint_id):
        try:
            return Complaint.objects.get(id=complaint_id)
        except Complaint.DoesNotExist:
            raise Http404

    def get(self, request, complaint_id, format=None):
        complaint =  Complaint.objects.get(id=complaint_id)
        serializer = ComplaintSerializer(complaint)
        return Response(serializer.data)

    def put(self, request, complaint_id, format=None):
        complaint = self.get_object(complaint_id)
        serializer = ComplaintAssignSerializer(complaint, data=request.data)
        if serializer.is_valid():
            if (ManagementUser.objects.filter(user = request.user).exists()):
                serializer.save(assigned_by=request.user)
            else:
                serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class ManagementView(APIView):
    def get(self, request, format=None):
        management_user = ManagementUser.objects.get(user = request.user)
        query_set = Complaint.objects.filter(category = management_user.category)
        serializer = ComplaintSerializer(query_set, many=True)
        return Response(serializer.data)



class DescriptionView(APIView):

    def get(self, request, complaint_id ,format=None):
        query_set = Management.objects.get(complaint=complaint_id)
        serializer = ComplaintAssignSerializer(query_set)
        return Response(serializer.data)

class CategoryView(APIView):
    def get(self, request, format=None):
        query_set = Category.objects.all()
        serializer = CategorySerializer(query_set, many=True)
        return Response(serializer.data)

class WorkerListView(APIView):
    def get(self, request, format=None):
        management_user = ManagementUser.objects.get(user = request.user)
        query_set = Worker.objects.filter(category = management_user.category)
        serializer = WorkerSerializer(query_set, many=True)
        return Response(serializer.data)

class WorkerComplaintView(APIView):
    def get(self, request, format=None):
        query_set = Complaint.objects.filter(assigned_to = request.user)
        serializer = ComplaintSerializer(query_set, many=True)
        return Response(serializer.data)
        
from .models import WorkerList
class CreateWorker(APIView):
    def post(self, request, format=None):
        serializer = CreateWorkerSerializer(data=request.data)
        management = ManagementUser.objects.get(user = request.user)

        if serializer.is_valid():
            serializer.save()
            worker = User.objects.get(username = request.data['username'])
            worker.set_password(request.data['username'])
            worker.save()
            Worker.objects.create(user = worker, category = management.category )
            WorkerList.objects.create(name = worker.first_name, number=worker.username ,category = management.category )

            # ##Mail
            # subject = "New Complaint from InstiMan"
            # from_email = settings.EMAIL_HOST_USER
            # msg = "image: "+"http://10.0.25.180:80"+serializer.data['image']+"\n"+"Description: " + serializer.data['description']+"\n"+"Location: "+ serializer.data['location']
            # send_mail(subject,msg,from_email,['sriraghu_malireddi@iitgn.ac.in',], fail_silently=False)

            return Response({'id': worker.id}, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

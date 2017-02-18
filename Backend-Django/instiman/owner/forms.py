from django import forms
from django.db import models

from complaint.models import Complaint,WorkerList,ManagerList,Category
from django.contrib.auth.models import User
from django.contrib.auth.forms import UserCreationForm


class ComplaintForm(forms.ModelForm):
    class Meta:
        model = Complaint
        fields = [
            "title",
            "location",
            "description",
            "assigned_to",
            "category",
            "status",
            "comment",
            # "category,name",
        ]

class WorkerForm(forms.ModelForm):
    class Meta:
        model = WorkerList
        fields = [
            "name",
            "number",
            "category",
        ]

class ManagerForm(forms.ModelForm):
    class Meta:
        model = ManagerList
        fields = [
            "name",
            "email",
            "category",
        ]


class CategoryForm(forms.ModelForm):
    class Meta:
        model = Category
        fields = [
            "name",
        ]

class UserCreateForm(UserCreationForm):
    email = forms.EmailField(required = True)

    class Meta:
        model = User
        fields = ['username', 'email', 'password1', 'password2']

        def save(self, commit=True):
            user = super(UserCreateForm, self).save(commit=False)
            user.email = self.cleaned_data('email')

            if commit:
                user.save()
            return user

class LogInForm(forms.Form):
    username = forms.CharField(label='User Name')
    password = forms.CharField(label='Password', widget = forms.PasswordInput)
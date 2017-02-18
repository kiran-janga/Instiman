# -*- coding: utf-8 -*-
# Generated by Django 1.9.4 on 2016-07-01 18:20
from __future__ import unicode_literals

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('complaint', '0006_complaint_comment'),
    ]

    operations = [
        migrations.AddField(
            model_name='complaint',
            name='assigned_by',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, related_name='assigned_by', to=settings.AUTH_USER_MODEL),
        ),
        migrations.AddField(
            model_name='complaint',
            name='assigned_to',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, related_name='assigned_to', to=settings.AUTH_USER_MODEL),
        ),
        migrations.AddField(
            model_name='complaint',
            name='priority',
            field=models.IntegerField(default=0),
        ),
        migrations.AddField(
            model_name='complaint',
            name='title',
            field=models.CharField(default='nothing', max_length=120),
            preserve_default=False,
        ),
    ]

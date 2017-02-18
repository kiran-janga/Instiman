# -*- coding: utf-8 -*-
# Generated by Django 1.9.4 on 2016-06-22 13:30
from __future__ import unicode_literals

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('management', '0008_remove_management_assigned'),
    ]

    operations = [
        migrations.CreateModel(
            name='MyDevice',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('dev_id', models.CharField(max_length=50, unique=True, verbose_name='Device ID')),
                ('reg_id', models.CharField(max_length=255, unique=True, verbose_name='Registration ID')),
                ('name', models.CharField(blank=True, max_length=255, null=True, verbose_name='Name')),
                ('creation_date', models.DateTimeField(auto_now_add=True, verbose_name='Creation date')),
                ('modified_date', models.DateTimeField(auto_now=True, verbose_name='Modified date')),
                ('is_active', models.BooleanField(default=False, verbose_name='Is active?')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='user_device', to=settings.AUTH_USER_MODEL)),
            ],
            options={
                'verbose_name_plural': 'Devices',
                'verbose_name': 'Device',
                'abstract': False,
                'ordering': ['-modified_date'],
            },
        ),
        migrations.AddField(
            model_name='log',
            name='comment',
            field=models.TextField(blank=True),
        ),
        migrations.AddField(
            model_name='management',
            name='attended',
            field=models.IntegerField(default=0),
        ),
        migrations.AddField(
            model_name='management',
            name='comment',
            field=models.TextField(blank=True),
        ),
    ]

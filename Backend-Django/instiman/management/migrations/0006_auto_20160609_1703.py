# -*- coding: utf-8 -*-
# Generated by Django 1.9.4 on 2016-06-09 17:03
from __future__ import unicode_literals

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('complaint', '0004_auto_20160609_1703'),
        ('management', '0005_auto_20160529_0836'),
    ]

    operations = [
        migrations.CreateModel(
            name='Log',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('time', models.DateTimeField(auto_now=True)),
                ('status', models.CharField(max_length=120)),
                ('complaint', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='complaint.Complaint')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.AddField(
            model_name='management',
            name='rating',
            field=models.IntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='management',
            name='timestamp',
            field=models.DateTimeField(auto_now_add=True),
        ),
    ]

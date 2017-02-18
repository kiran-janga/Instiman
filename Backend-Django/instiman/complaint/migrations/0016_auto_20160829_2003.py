# -*- coding: utf-8 -*-
# Generated by Django 1.9.7 on 2016-08-29 14:33
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('complaint', '0015_managerlist_workerlist'),
    ]

    operations = [
        migrations.AddField(
            model_name='managerlist',
            name='category',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to='complaint.Category'),
        ),
        migrations.AddField(
            model_name='workerlist',
            name='category',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, to='complaint.Category'),
        ),
    ]
# -*- coding: utf-8 -*-
# Generated by Django 1.9.4 on 2016-05-09 04:46
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('complaint', '0002_auto_20160509_0358'),
        ('management', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='management',
            name='complaint',
            field=models.ForeignKey(default=1, on_delete=django.db.models.deletion.CASCADE, related_name='complaint', to='complaint.Complaint'),
            preserve_default=False,
        ),
    ]

# -*- coding: utf-8 -*-
# Generated by Django 1.9.4 on 2016-06-20 07:03
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('management', '0007_management_assigned'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='management',
            name='assigned',
        ),
    ]

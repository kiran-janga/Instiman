# -*- coding: utf-8 -*-
# Generated by Django 1.9.4 on 2016-07-31 09:18
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('complaint', '0010_auto_20160704_1047'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='complaint',
            name='assigned',
        ),
    ]

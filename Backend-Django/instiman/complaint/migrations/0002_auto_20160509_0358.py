# -*- coding: utf-8 -*-
# Generated by Django 1.9.4 on 2016-05-09 03:58
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('complaint', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='complaint',
            name='image',
            field=models.FileField(blank=True, null=True, upload_to=''),
        ),
        migrations.AlterField(
            model_name='complaint',
            name='status',
            field=models.CharField(default='Submitted', max_length=20),
        ),
    ]

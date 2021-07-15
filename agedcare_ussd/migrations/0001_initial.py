# Generated by Django 3.0 on 2019-12-18 14:12

import django.contrib.postgres.fields.jsonb
from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Session',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('gateway', models.CharField(max_length=30)),
                ('nw_code', models.CharField(max_length=50)),
                ('session_id', models.CharField(max_length=50)),
                ('service_code', models.CharField(max_length=50)),
                ('phone_number', models.CharField(max_length=50)),
                ('msg_type', models.CharField(max_length=50)),
                ('operator', models.CharField(max_length=15)),
                ('stage', models.CharField(max_length=50, null=True)),
                ('aggregated_data', django.contrib.postgres.fields.jsonb.JSONField(default=dict)),
                ('created_at', models.DateTimeField(auto_now_add=True)),
            ],
        ),
    ]

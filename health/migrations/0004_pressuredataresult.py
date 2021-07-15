# Generated by Django 3.0 on 2020-05-05 22:21

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('health', '0003_auto_20200505_1921'),
    ]

    operations = [
        migrations.CreateModel(
            name='PressureDataResult',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('phonenumber', models.CharField(max_length=50, verbose_name='Phone Number')),
                ('result', models.CharField(max_length=50, verbose_name='Sysotlic / Diasytolic')),
                ('task_category', models.CharField(choices=[('select option', 'select option'), ('weekly', 'weekly'), ('monthly', 'monthly')], default='select option', max_length=50, verbose_name='Category')),
                ('sms_category', models.CharField(choices=[('select option', 'select option'), ('processing', 'processing'), ('delivered', 'delivered'), ('sent', 'sent'), ('failed', 'failed')], default='select option', max_length=50, verbose_name='Sms Status')),
                ('created_at', models.DateTimeField(auto_now=True, verbose_name='Created At')),
            ],
        ),
    ]
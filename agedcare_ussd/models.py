from django.db import models
from django.contrib.postgres.fields import JSONField



class Session(models.Model):
	gateway = models.CharField(max_length=30)
	nw_code = models.CharField(max_length=50)
	session_id = models.CharField(max_length=50)
	service_code = models.CharField(max_length=50)
	phone_number = models.CharField(max_length=50)
	msg_type = models.CharField(max_length=50)
	operator = models.CharField(max_length=15)
	stage = models.CharField(max_length=50,null=True)
	aggregated_data = JSONField(default=dict)
	created_at = models.DateTimeField(auto_now_add=True, blank=True)


	def set_stage(self, stage_name):
		self.stage = stage_name
		self.save()

	def save_data(self,data):
		self.aggregated_data.update(data)
		self.save()
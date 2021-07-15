#!/usr/bin/env python
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny
from rest_framework import status 

from agedcare_ussd.ussd.processor import Ussd



import json



class Hubtel(APIView):
	permission_classes = (AllowAny, )
	response = ''



	def post(self, request, *args, **kwargs):
		data = json.loads(request.body)
		response_type = data.get('Type')
		print('hubtel response type: {}'.format(response_type))
		response_type_cast = dict(
			Response='response',
			Timeout='timeout',
			Cancelled='cancelled',
			Initiation='initiation',
			Release='release'

			)
		session_data = dict(
			phone_number=data.get('Mobile'),
			session_id=data.get('SessionId'),
			service_code=data.get('ServiceCode'),
			response_type=response_type_cast.get(response_type),
			operator=data.get('Operator'),
			message=data.get('Message'),
			sequence=data.get('Sequence'),
			gateway='hubtel',
			initiation='Initiation',
			release='Release',
			response='Response'
			)
	

		ussd_instance = Ussd(data=session_data)
		response_data = ussd_instance.process_request()
		print('response data: {}'.format(response_data))
		hubtel_response_type = session_data.get(response_data.get('response_type'))
		
		return Response(dict(Message=response_data.get('message'),ClientState=response_data.get('stage'),Type=hubtel_response_type))


class AfricaTalking(APIView):
	permission_classes = (AllowAny, )
	response = '' 

	def post(self, request, *args, **kwargs):
		data = json.loads(request.body)
		response_type = data.get('Type')

		response_type_cast = dict(
			Response='response',
			Timeout='timeout',
			Cancelled='cancelled',
			Initiation='initiation',
			Release='release'
		)

		session_data = dict(
			phone_number=data.get('phoneNumber'),
			session_id=data.get('sessionId'),
			service_code=data.get('serviceCode'),
			response_type=response_type_cast.get(response_type),
			operator=data.get('Operator'),
			message=data.get('text'),
			sequence=data.get('Sequence'),
			gateway='africatalking',
			initiation='Initiation',
			release='Release',
			response='Response'
		)

		
		ussd_instance = Ussd(data=session_data)
		response_data = ussd_instance.process_request()
		print('response data: {}'.format(response_data))
		hubtel_response_type = session_data.get(response_data.get('response_type'))
		
		return Response(dict(Message=response_data.get('message'),ClientState=response_data.get('stage'),Type=hubtel_response_type))
# USSD Payment Asoriba
import requests
import json
import logging
import uuid

class Asoriba:
    
    def __init__(self, pub_key, webhook_url, product_name, description=None):
        self.pub_key = pub_key
        self.webhook_url = webhook_url
        self.product_name = product_name
        self.description = description
        
    def detect_network(self, msisdn):
        match = msisdn[:3]
        network = ""
        
        if match in ['024', '054', '055', '059']:
            network = "mtn_gh"    
            
        elif match in ['027', '057', '026', '056']:
            network = "airtel_gh"
            
        elif match in ['020', '050']:
            network = "vodafone_gh"
        else:
            network = "Unknown Network"
        
        return network, msisdn
    
        
    def initialize(self, amount, first_name, last_name, phone_number, email=None, vodafone_token=None):
        
        try:
            network, msisdn = self.detect_network(phone_number)
            req = requests.post("https://payment.asoriba.com/payment/v1.0/initialize",
                            headers={
                                    "Accept": "application/json",
                                    "Content-Type": "application/json",
                                    "Authorization": "Bearer " + self.pub_key
                            },
                            json={
                                    "amount": amount,
                                    "metadata": {
                                        "order_id": str(uuid.uuid1()),
                                        "product_name": self.product_name,
                                        "product_description": self.description
                                        },
                                    "callback": self.webhook_url,
                                    "post_url": self.webhook_url,
                                    "pub_key":  self.pub_key,
                                    "order_image_url":"https://app.mybusinesspay.com/assets/asoribalogo-3d4540003815aee230ca676138579ed495cfa975270fe2d7e656292c4508d472.png",
                                    "capture": "true",
                                    "first_name": first_name,
                                    "last_name": last_name,
                                    "email": email,
                                    "phone_number": msisdn,
                                    "payment_method": network,
                                    "vodafone_token": vodafone_token
                            })
            response = req.json()
            return response["status"]
        except Exception as e:
            print(e)
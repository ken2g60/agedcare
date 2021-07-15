from agedcare_ussd.models import Session
from agedcare_ussd.ussd.constants import SERVICE_CODE ,PHONE_NUMBER ,SESSION_ID  ,OPERATOR ,SEQUENCE ,MESSAGE ,CLIENT_STATE , RESPONSE, TIMEOUT , RELEASE , GATEWAY, RESPONSE_TYPE , INITIATION , START, MAIN_MENU, ONE,TWO,THREE,FOUR, STAGE_ENTER_FULLNAME , STAGE_ENTER_TELEPHONE , STAGE_SELECT_GENDER , STAGE_ENTER_AMOUNT , STAGE_ENTER_MOMO , STAGE_BLOOD_SUGAR , STAGE_BLOOD_PRESSURE , STAGE_BLOOD_CHOLESTEROL , STAGE_BLOOD_LEVEL , STAGE_WEIGHT_LEVEL , STAGE_HELP , FINISH, STAGE_SAVING_AMOUNT, VERIFY_USER_ID, STAGE_USER_ID, STAGE_BLOOD_SUGAR
from django.core.exceptions import ObjectDoesNotExist, MultipleObjectsReturned



from careapp.models import UserModel
from health.models import HealthData
from django.db.models import Q


from pyhubtel_sms import SMS
sms = SMS(client_id='', client_secret='')

class Ussd(object):

    def __init__(self, data):
        self.service_code = data.get(SERVICE_CODE)
        self.phone_number = data.get(PHONE_NUMBER)
        self.session_id = data.get(SESSION_ID)
        self.operator = data.get(OPERATOR)
        self.sequence = data.get(SEQUENCE)
        self.message = data.get(MESSAGE)
        self.stage = data.get(CLIENT_STATE)
        self.response = data.get(RESPONSE_TYPE)
        self.gateway = data.get(GATEWAY)
        self.session = self.get_or_create_session()
    

    def get_stage(self):
        return self.session.stage
    
    def get_or_create_session(self):
        session, created = Session.objects.get_or_create(session_id=self.session_id,gateway=self.gateway,phone_number=self.phone_number,defaults=dict(operator=self.operator))
        if created:
            session.set_stage(START)
        return session
    

    def save_data(self, data):
        try:

            record, created = Session.objects.update_or_create(session_id=self.session_id,
                                                                service_code=self.service_code,
                                                                operator=self.operator,
                                                                phone_number=self.phone_number,
                                                                stage=self.stage)
            record.save_data(data)
        except Session.DoesNotExist:
            # log error
            pass
    

    def finish(self, process=None):
        
        text = '' 
        response_type = RELEASE
        client_state = FINISH

        if process == 'registration':
            # send sms to the register user phone number with user id
            # msg to user_id.userId
            user_id = UserModel.objects.filter(session_phonenumber=self.phone_number)
            
            # message = Message(
            #     sender='userids',
            #     content='Your User ID {}. Please keep it secret'.format(user_id),
            #     recipient=self.phone_number,
            #     registered_delivery=True,
            # )
            # sms.send(message)
            
            text = 'Thank You for Registering'
            
        elif process == 'saving':
            text = 'Your Deposit is Successfully Done '
        
        elif process == 'health':
            text = 'Self Check Results Submitted Successfully'
        
        elif process == 'performance':
            text = 'Your Performance will be send to you'
        
        elif process == 'existing_account':
            text = 'Phone Number is already registered'
            
        return self.process_response(message=text,response_type=response_type,client_state=client_state)
    
    def process_request(self):

        if self.response == INITIATION:
            return self.welcome_option()
        elif self.response == RESPONSE:
            stage = self.get_stage()
            if stage == MAIN_MENU:
                return self.main_menu_options()
            else:
                return self.get_next_response(client_state=stage)
    
    def get_next_response(self, client_state):
        
        if client_state == STAGE_ENTER_FULLNAME:

            if self.message == '':
                return self.register_enter_fullname(error=True)
            
            # if UserModel.objects.filter(~Q(session_phonenumber=self.phone_number)):
            #     return self.finish(process='existing_account')
            
            self.save_data({'fullname': self.message})
            return self.register_enter_telephone()
        

        elif client_state == STAGE_ENTER_TELEPHONE:

            if self.message == '':
                return self.register_enter_telephone(error=True)
            
            self.save_data({'phonenumber': self.message})
            return self.register_enter_gender()
        
        elif client_state == STAGE_SELECT_GENDER:

            if self.message == '':
                return self.register_enter_gender(error=True)

            self.save_data({'gender': self.message})
            return self.register_finish()
        
        
        elif client_state == STAGE_USER_ID:

            if UserModel.objects.filter(~Q(userId=self.message)):
                return self.saving_user_id(error=True)
            
            if self.message == '':
                return self.saving_amount(error=True)
            
            self.save_data({'userId': self.message})
            return self.saving_momo()
        
        
        elif client_state == STAGE_ENTER_MOMO:

            if self.message == '':
                return self.saving_momo(error=True)
            
            self.save_data({'momo': self.message})
            return self.finish_saving()
        
        
        
        elif client_state == VERIFY_USER_ID:
            
        
            if UserModel.objects.filter(~Q(userId=self.message)):
                return self.verifiy_user_id(error=True)
            
            self.save_data({'userId': self.message})
            return self.blood_sugar()
        
        
        
        elif client_state == STAGE_BLOOD_SUGAR:

            if self.message == '':
                return self.blood_sugar(error=True)
            
            self.save_data({'bloodsugar': self.message})
            return self.blood_pressure()
        
        elif client_state == STAGE_BLOOD_PRESSURE:

            if self.message == '':
                return self.blood_pressure(error=True)
            
            
            self.save_data({'bloodpressure': self.message})
            return self.blood_cholesterol()
        
        elif client_state == STAGE_BLOOD_CHOLESTEROL:

            if self.message == '':
                return self.blood_cholesterol(error=True)
            
            self.save_data({'bloodcholesterol':  self.message})
            return self.blood_level()
        

        elif client_state == STAGE_BLOOD_LEVEL:

            if self.message == '':
                return self.blood_level(error=True)
            
            self.save_data({'bloodlevel': self.message})
            return self.weight()
        
        elif client_state == STAGE_WEIGHT_LEVEL:

            if self.message == '':
                return self.weight(error=True)
            
            self.save_data({'weight': self.message})
            return self.finish_record()
            
            

    def welcome_option(self):
        return self.display_main_menu()
    
    def main_menu_options(self):
        if self.message in [ONE,TWO,THREE,FOUR]:

            if self.message == ONE:
                return self.register_enter_fullname()

            elif self.message == TWO:
                return self.verifiy_user_id()
            
            elif self.message == THREE:
                return self.saving_user_id()
            
            elif self.message == FOUR:
                return self.display_performance()

        else:
            return self.display_main_menu(error=True)
    
    def display_main_menu(self, error=False):
        message = 'Welcome to AgedCare \n 1. Register \n 2. Self-check results \n 3. Health Fund \n 4. Health Performance'

        if error:
            message = 'Invalid Option Selected \n 1. Register \n 2. AgedCare \n 3. Health Fund \n 4. Health Performance'
        
        stage = MAIN_MENU
        response_type = RESPONSE
        return self.process_response(message=message,response_type=response_type,client_state=stage)
    

    def register_enter_fullname(self, error=False):

        if error:
            message = 'Enter Full Name'


        stage = STAGE_ENTER_FULLNAME
        response_type = RESPONSE
        message = 'Enter Full Name'
        return self.process_response(message=message,response_type=response_type,client_state=stage)


    def register_enter_telephone(self, error=False):

        if error:
            message = 'Invalid Enter Phone Number'
        stage = STAGE_ENTER_TELEPHONE
        response_type = RESPONSE
        message = 'Enter Phone Number'
        return self.process_response(message=message,response_type=response_type,client_state=stage)

    def register_enter_gender(self, error=False):
        
        if error:
            message = 'Invalid Option. Select Gender Type \n 1. M (Male) \n 2. F (Female)'
        
        stage = STAGE_SELECT_GENDER
        response_type = RESPONSE
        message = 'Gender Type \n 1. M (Male) \n 2. F (Female)'
        return self.process_response(message=message,response_type=response_type,client_state=stage)
    
    def register_finish(self, error=False):
        
        session_data = Session.objects.filter(session_id=self.session_id).latest('created_at')
        aggregated_data = session_data.aggregated_data
        
        UserModel.objects.create(
            session_phonenumber=self.phone_number,
            fullname=aggregated_data.get('fullname'),
            phonenumber=aggregated_data.get('phonenumber'),
            gender=aggregated_data.get('gender')
        )
        return self.finish(process='registration')
        

    
    
    def verifiy_user_id(self, error=False):
        
        if error:
            message = 'Invalid User ID'
        
        stage = VERIFY_USER_ID
        response_type = RESPONSE
        message = 'Enter Your User ID'
        return self.process_response(message=message,response_type=response_type,client_state=stage)
    

    def blood_sugar(self, error=False):

        if error:
            message = 'Enter Your Blood Sugar'
        

        stage = STAGE_BLOOD_SUGAR
        response_type = RESPONSE
        message = 'Enter Your Blood Sugar'
        return self.process_response(message=message,response_type=response_type,client_state=stage)

    def blood_pressure(self, error=False):

        if error:
            message = 'Enter Your Blood Pressure'
        

        stage = STAGE_BLOOD_PRESSURE
        response_type = RESPONSE
        message = 'Enter Your Blood Pressure'
        return self.process_response(message=message,response_type=response_type,client_state=stage)
    

    def blood_cholesterol(self, error=False):

        if error:
            message = 'Enter Your Blood Cholesterol'
        
        stage = STAGE_BLOOD_CHOLESTEROL
        response_type = RESPONSE
        message = 'Enter Your Blood Cholesterol'
        return self.process_response(message=message,response_type=response_type,client_state=stage)
    

    def blood_level(self, error=False):

        if error:
            message = 'Enter Your Blood Level'
        
        stage = STAGE_BLOOD_LEVEL
        response_type = RESPONSE
        message = 'Enter Your Blood Level'
        return self.process_response(message=message,response_type=response_type,client_state=stage)
    

    def weight(self, error=False):

        if error:
            message = 'Enter Your Weight'
        

        stage = STAGE_WEIGHT_LEVEL
        response_type = RESPONSE
        message = 'Enter Your Weight'
        return self.process_response(message=message,response_type=response_type,client_state=stage)

    def finish_record(self, error=False):
        
        session_data = Session.objects.filter(session_id=self.session_id).latest('created_at')
        aggregated_data = session_data.aggregated_data

        HealthData.objects.create(
            phonenumber=self.phone_number,
            bloodsugar=aggregated_data.get('bloodsugar'),
            bloodpressure=aggregated_data.get('bloodpressure'),
            bloodcholesterol=aggregated_data.get('bloodcholesterol'),
            bloodlevel=aggregated_data.get('bloodlevel'),
            weight=aggregated_data.get('weight')
        )
        return self.finish(process='health')
        
    
    # health fund
    def saving_user_id(self, error=False):
        

        if error:
            message = 'Invalid  User ID'
        
        stage = STAGE_USER_ID
        response_type = RESPONSE
        message = 'Enter User ID '
        return self.process_response(message=message,response_type=response_type,client_state=stage)
    
    
    def saving_momo(self, error=False):

        if error:
            message = 'Enter Mobile Money Number'
        
        stage = STAGE_ENTER_MOMO
        response_type = RESPONSE
        message = 'Enter Mobile Money Number'
        return self.process_response(message=message,response_type=response_type,client_state=stage)
    
    
    def saving_amount(self, error=False):
        
        if error:
            message = 'Enter Your Amount'
        
        
        stage = STAGE_SAVING_AMOUNT
        response_type = RESPONSE
        message = 'Enter Amount To Save'
        return self.process_response(message=message,response_type=response_type,client_state=stage)

    
    def finish_saving(self, error=False):
        
        session_data = Session.objects.filter(session_id=self.session_id).latest('created_at')
        aggregated_data = session_data.aggregated_data

        
        ContributionModel.objects.create(
            momo=aggregated_data.get('amount'),
            amount=aggregated_data.get('momo')
        )
        return self.finish(process='saving')

    
    def display_performance(self, error=False):
        return self.finish(process='performance')

        
    def help(self, error=False):
        stage = STAGE_HELP
        response_type = RELEASE
        message = 'Contact Phone Number'
        return self.process_response(message=message,response_type=response_type,client_state=stage)

    def exit(self):
        message = 'Thank you '
        client_state = FINISH
        response_type = RELEASE
        return self.process_response(message=message,response_type=response_type,client_state=client_state)
    
    def process_response(self, message, response_type, client_state):
        session = self.session
        session.set_stage(client_state)
        return dict(message=message, stage=client_state,response_type=response_type)

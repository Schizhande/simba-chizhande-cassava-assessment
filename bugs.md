
==============ERRORS================

1. Logger error
-----> non-static variable LOGGER cannot be referenced from a static context

Solution :: 'LOGGER' has been changed from an instance variable to a static variable so that it can be used on static methods.

2. SubscriberRequest 
------> The annotation @PreInsert could not be found. This class does not exist in the hibernate persistence library, which is 
        why the error occurs.

Solution:: replace @PreInsert with @PrePersist

3. PartnerCodeValidatorImpl 
------> incorrect syntax error on super call

Solution:: correct it to super()

4. CreditsServiceImpl 
------> persist and upgrade methods  not be found in the SubscriberRequestDao repository. No update and persist methods in Spring Data JPA

Solution:: replace the method with the save() method. save() method is used to save/insert/update entity data in Spring JPA

5. EnquiriesServiceImpl 
------> Persist and update method not found in SubscriberRequestDao repository. No update and persist methods in Spring Data JPA

Solution:: replace the method with the save() method. save() method is used to save/insert/update entity data in Spring JPA

6. IntelligentNetworkServiceImpl 
------> Unsupported Media Type when accessing the web service API

Solution:: remove SOAP12 binding

7. IntelligentNetworkService
------> Error 500

Solution::  add @WebParam annotation on partner code parameter for enquire balance webservice API

8. electronic-payments-api
------> Request Interceptor Error. incorrect 'and' operator.
   
Solution:: replaced 'and' with &&

9. EpayResource:
-----> Failed to inject beans in EpayResource controller
 Solution:: By using the @Autowired annotation, injected the EpayRequestProcessor and ReportingProcessor beans. 

10. SubscriberRequest Named query error
-----> Request is not mapped
Solution:: rename Request to request in query

11. Partner code path variable not annotated
solution:: Add @PathVariable annotation on partnerCode parameter.

12. Null pointer exception in getting Response codes
-----> The enum constructor parameter was assigned to itself, therefore the instance variable code will be assigned to default value null

Solution:: fixed the Response code constructor. When the same name used for both a parameter and a variable in a 
constructor and setter method, this should be used to refer to the instance variable.

13. 

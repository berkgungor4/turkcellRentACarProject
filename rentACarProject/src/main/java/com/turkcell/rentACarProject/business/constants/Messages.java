package com.turkcell.rentACarProject.business.constants;

public class Messages {
	
	public static final String BUSINESS_EXCEPTION_ERRORS = "BusinessException.Errors";
	public static final String SUCCESS = "Success";
	public static final String VALIDATION_ERRORS = "Validation.Errors";

	public static final String ADDITIONAL_SERVICE_ADD = "AdditionalService.Added";
	public static final String ADDITIONAL_SERVICE_UPDATE = "AdditionalService.Updated";
	public static final String ADDITIONAL_SERVICE_DELETE = "AdditonalService.Deleted";
	public static final String ADDITIONAL_SERVICE_NOT_FOUND = "Cannot find an additional service with this Id";

	public static final String BRAND_ADD = "Brand.Added";
	public static final String BRAND_UPDATE = "Brand.Updated";
	public static final String BRAND_DELETED = "Brand.Deleted";
	public static final String BRAND_NOT_FOUND = "Cannot find a brand with this Id";
	
	public static final String CAR_DAMAGE_ADD = "CarDamage.Added";
	public static final String CAR_DAMAGE_UPDATE = "CarDamage.Updated";
	public static final String CAR_DAMAGE_DELETE = "CarDamage.Deleted";
	public static final String CAR_DAMAGE_NOT_FOUND = "Cannot find a car damage with this Id";

	public static final String CAR_MAINTENANCE_ADD = "CarMaintenance.Added";
	public static final String CAR_MAINTENANCE_UPDATE = "CarMaintenance.Updated";
	public static final String CAR_MAINTENANCE_DELETE = "CarMaintenance.Deleted";
	public static final String CAR_MAINTENANCE_NOT_FOUND = "Cannot find a car maintenance with this Id";
	public static final String CAR_MAINTENANCE_CAR_IN_RENT = "Maintenance  can't be added (Car is Rented at requested times)";

	public static final String CAR_ADD = "Car.Added";
	public static final String CAR_UPDATE = "Car.Updated";
	public static final String CAR_DELETE = "Car.Deleted";
	public static final String CAR_NOT_FOUND = "Cannot find a car with this Id";
	
	public static final String CITY_ADD = "City.Added";
	public static final String CITY_UPDATE = "City.Updated";
	public static final String CITY_DELETE = "City.Deleted";
	public static final String CITY_NOT_FOUND = "Cannot find a city with this Id";

	public static final String COLOR_ADD = "Color.Added";
	public static final String COLOR_UPDATE = "Color.Updated";
	public static final String COLOR_DELETE = "Color.Deleted";
	public static final String COLOR_EXISTS = "This color is already used";
	public static final String COLOR_NOT_FOUND = "Cannot find a color with this Id";

	public static final String CORPORATE_CUSTOMER_ADD = "CorporateCustomer.Added";
	public static final String CORPORATE_CUSTOMER_UPDATE = "CorporateCustomer.Updated";
	public static final String CORPORATE_CUSTOMER_DELETE = "CorporateCustomer.Deleted";
	public static final String CORPORATE_CUSTOMER_NOT_FOUND = "Cannot find a corporate customer with this Id";
	
	public static final String CREDIT_CARD_ADD = "CreditCard.Added";
	public static final String CREDIT_CARD_UPDATE = "CreditCard.Updated";
	public static final String CREDIT_CARD_DELETE = "CreditCard.Deleted";
	public static final String CREDIT_CARD_EXISTS = "This credit card is already used";

	public static final String INDIVIDUAL_CUSTOMER_ADD = "IndividualCustomer.Added";
	public static final String INDIVIDUAL_CUSTOMER_UPDATE = "IndividualCustomer.Updated";
	public static final String INDIVIDUAL_CUSTOMER_DELETE = "IndividualCustomer.Deleted";
	public static final String INDIVIDUAL_CUSTOMER_NOT_FOUND = "Cannot find a individual customer with this Id";

	public static final String CUSTOMER_NOT_FOUND = "Cannot find a customer with this Id";

	public static final String INVOICE_ADD = "Invoice.Added";
	public static final String INVOICE_UPDATE = "Invoice.Updated";
	public static final String INVOICE_DELETE = "Invoice.Deleted";
	public static final String INVOICE_NOT_FOUND = "Cannot find a invoice with this Id";
	
	public static final String ORDERED_ADDITIONAL_SERVICE_ADD = "OrderedAdditionalService.Added";
	public static final String ORDERED_ADDITIONAL_SERVICE_UPDATE = "OrderedAdditionalService.Updated";
	public static final String ORDERED_ADDITIONAL_SERVICE_DELETE = "OrderedAdditonalService.Deleted";
	public static final String ORDERED_ADDITIONAL_SERVICE_NOT_FOUND = "Cannot find an ordered additional service with this Id";
	
	public static final String PAYMENT_ADD = "Payment.Added";
	public static final String PAYMENT_DELETE = "Payment.Deleted";
	public static final String PAYMENT_NOT_FOUND = "Cannot find a payment with this Id";

	public static final String RENTAL_ADD = "Rental.Added";
	public static final String RENTAL_UPDATE = "Rental.Updated";
	public static final String RENTAL_DELETE = "Rental.Deleted";
	public static final String RENTAL_NOT_FOUND = "Cannot find a rental with this Id";
	public static final String RENTAL_IN_MAINTENANCE = "Rental can't be added (Car is under maintenance at requested times";
	public static final String RENTAL_RETURN_DATE_ERROR = "Return date can't be earlier than rental date";

}

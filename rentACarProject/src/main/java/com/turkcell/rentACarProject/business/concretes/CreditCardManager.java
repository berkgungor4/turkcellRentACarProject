package com.turkcell.rentACarProject.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CreditCardService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.requests.creditCard.CreateCreditCardRequest;
import com.turkcell.rentACarProject.business.requests.creditCard.DeleteCreditCardRequest;
import com.turkcell.rentACarProject.business.requests.creditCard.UpdateCreditCardRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CreditCardDao;
import com.turkcell.rentACarProject.entities.concretes.CreditCard;

@Service
public class CreditCardManager implements CreditCardService {
	
	private CreditCardDao creditCardDao;
	private ModelMapperService modelMapperService;
	
	@Autowired
	public CreditCardManager(CreditCardDao creditCardDao, ModelMapperService modelMapperService) {
		this.creditCardDao = creditCardDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public Result create(CreateCreditCardRequest createCreditCardRequest) {
		
		checkIfCardExists(createCreditCardRequest.getCardNumber());
		
		CreditCard creditCard = this.modelMapperService.forRequest().map(createCreditCardRequest, CreditCard.class);
		
		creditCard.setId(0);
		
		this.creditCardDao.save(creditCard);
		
		return new SuccessResult(Messages.CREDIT_CARD_ADD);
	}

	@Override
	public Result update(UpdateCreditCardRequest updateCreditCardRequest) {
		
		CreditCard creditCard = this.modelMapperService.forRequest().map(updateCreditCardRequest, CreditCard.class);
		this.creditCardDao.save(creditCard);
		return new SuccessResult(Messages.CREDIT_CARD_UPDATE);
	}

	@Override
	public Result delete(DeleteCreditCardRequest deleteCreditCardRequest) {
		
		CreditCard creditCard = this.modelMapperService.forRequest().map(deleteCreditCardRequest, CreditCard.class);
		this.creditCardDao.delete(creditCard);
		return new SuccessResult(Messages.CREDIT_CARD_DELETE);
	}
	
	private boolean checkIfCardExists(String cardNumber) {

		if (this.creditCardDao.existsByCardNumber(cardNumber)) {
			throw new BusinessException(Messages.CREDIT_CARD_EXISTS);
		}
		return true;
	}
	
}

package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.ColorService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.color.ListColorDto;
import com.turkcell.rentACarProject.business.requests.color.CreateColorRequest;
import com.turkcell.rentACarProject.business.requests.color.DeleteColorRequest;
import com.turkcell.rentACarProject.business.requests.color.UpdateColorRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.ColorDao;
import com.turkcell.rentACarProject.entities.concretes.Color;

@Service
public class ColorManager implements ColorService {

	private ColorDao colorDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public ColorManager(ColorDao colorDao, ModelMapperService modelMapperService) {
		this.colorDao = colorDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<ListColorDto>> getAll() {
		List<Color> result = this.colorDao.findAll();
		List<ListColorDto> response = result.stream()
				.map(color -> this.modelMapperService.forDto().map(color, ListColorDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<ListColorDto>> (response, Messages.SUCCESS);
	}
	
	@Override
	public DataResult<ListColorDto> getById(int id) {
		Color result = this.colorDao.getColorById(id);
		ListColorDto response = this.modelMapperService.forDto().map(result, ListColorDto.class);
		return new SuccessDataResult<ListColorDto>(response, Messages.SUCCESS);
	}
	
	@Override
	public Result create(CreateColorRequest createColorRequest) {
		
		checkIfColorNameExists(createColorRequest.getName());
		
		Color color = this.modelMapperService.forRequest().map(createColorRequest, Color.class);
		this.colorDao.save(color);
		return new SuccessResult(Messages.COLOR_ADD);
	}

	@Override
	public Result delete(DeleteColorRequest deleteColorRequest) {
		
		Color color = this.modelMapperService.forRequest().map(deleteColorRequest, Color.class);
		this.colorDao.delete(color);
		return new SuccessResult(Messages.COLOR_DELETE);
	}

	@Override
	public Result update(UpdateColorRequest updateColorRequest) {
		
		checkIfColorExists(updateColorRequest.getId());
		
		Color color = this.modelMapperService.forRequest().map(updateColorRequest, Color.class);
		this.colorDao.save(color);
		return new SuccessResult(Messages.COLOR_UPDATE);
	}
	
	private Color checkIfColorExists(int id){
		
		Color color = this.colorDao.getColorById(id);
		
		if (color == null) {
			throw new BusinessException(Messages.COLOR_NOT_FOUND);
		}
		return color;
		
	}
		
		private boolean checkIfColorNameExists(String name){
			
			if (this.colorDao.getColorByName(name) == null) {
				return true;
			}
			throw new BusinessException(Messages.COLOR_EXISTS);
		}
	
}

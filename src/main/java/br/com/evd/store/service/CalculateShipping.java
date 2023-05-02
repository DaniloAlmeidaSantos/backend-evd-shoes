package br.com.evd.store.service;

import org.springframework.stereotype.Service;

import br.com.evd.store.model.dto.CalculateShippingRequestModelDTO;
import br.com.evd.store.model.dto.CalculateShippingResponseModelDTO;

@Service
public interface CalculateShipping {
	CalculateShippingResponseModelDTO getPriceWithFreight(CalculateShippingRequestModelDTO request);
}

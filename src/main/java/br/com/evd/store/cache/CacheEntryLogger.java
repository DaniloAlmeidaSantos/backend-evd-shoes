package br.com.evd.store.cache;

import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryExpiredListener;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheEntryLogger implements CacheEntryCreatedListener<Object, Object>,
		CacheEntryExpiredListener<Object, Object>, CacheEntryUpdatedListener<Object, Object> {
	
	@Override
	public void onUpdated(Iterable<CacheEntryEvent<?, ?>> iterable)
			throws CacheEntryListenerException {
		iterable.forEach(this::logEvent);
	}

	@Override
	public void onExpired(Iterable<CacheEntryEvent<?, ?>> iterable)
			throws CacheEntryListenerException {
		iterable.forEach(this::logEvent);
	}

	@Override
	public void onCreated(Iterable<CacheEntryEvent<?, ?>> iterable)
			throws CacheEntryListenerException {
		iterable.forEach(this::logEvent);
	}
	
	private void logEvent(CacheEntryEvent<?, ?> event) {
		log.info(
			"eventType={} cache={} cacheKey={} oldValue={} newValue={}",
			event.getEventType(),
			event.getSource().getName(),
			event.getKey(),
			event.getOldValue(),
			event.getValue()
		);
	}

}

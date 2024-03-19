package rs.edu.raf.StockService.services.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.services.CurrencyService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class InMemoryCurrencyServiceImpl implements CurrencyService {
    private List<Currency> currencyList;
    private final RedisTemplate<String, Currency> redisTemplate;


    public InMemoryCurrencyServiceImpl(RedisTemplate<String, Currency> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    @Cacheable(value = "currency")
    public List<Currency> findAll() {
     /* redis impl
     Set<String> currencyKeys = redisTemplate.keys("currency:*");
      assert currencyKeys != null;
        List<Object> currencies = currencyKeys.stream()
                .map(key -> redisTemplate.opsForValue().get(key))
                .collect(Collectors.toList());
        System.out.println(currencies.size());
        return currencies.stream().map(currency ->
                (Currency) currency).toList();*/
        return currencyList;
    }

    @Cacheable(value = "currency", key = "#id")
    public Currency findById(Long id) {
      /* redis impl
        Set<String> key = redisTemplate.keys("currency:* id=" + id);
        assert key != null;
        System.out.println(key.size());
        System.out.println(key);
        return redisTemplate.opsForValue().get(key.stream().findFirst().orElseThrow(RuntimeException::new));

       */
        System.out.println("ULAZI OVDE OVDE");
        return currencyList.stream().filter(currency -> currency.getId() == (id)).findFirst().orElse(null);
    }

    @Cacheable(value = "currency", key = "#currencyCode")
    public Currency findByCurrencyCode(String currencyCode) {
      /* redis impl
       Set<String> key = redisTemplate.keys("currency:code=" + currencyCode);
        assert key != null;
        System.out.println(key.size());
        System.out.println(key);
        return redisTemplate.opsForValue().get(key.stream().findFirst().orElseThrow(RuntimeException::new));*/
        return currencyList.stream().filter(currency -> currency.getCurrencyCode().equalsIgnoreCase(currencyCode)).findFirst().orElse(null);
    }

    @Cacheable(value = "currency", key = "#currencyName")
    public Currency findByCurrencyName(String currencyName) {
        return currencyList.stream().filter(currency -> currency.getCurrencyName().equalsIgnoreCase(currencyName)).findFirst().orElse(null);
    }
}

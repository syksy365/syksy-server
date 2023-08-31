package site.syksy.rose.setting.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Raspberry
 * @since 2021-02-13
 */
@RestController
@RequestMapping("setting/cache")
public class CacheController {

    @Autowired
    CacheManager cacheManager;

    @DeleteMapping
    @Operation(summary = "清除全局缓存")
    public void delete() {
        cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

}
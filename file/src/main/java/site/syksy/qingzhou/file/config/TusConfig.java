package site.syksy.qingzhou.file.config;

import me.desair.tus.server.TusFileUploadService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.syksy.qingzhou.common.properties.QingZhouProperties;

/**
 * @author Raspberry
 */
@Configuration
public class TusConfig {

    @Bean
    public TusFileUploadService tusFileUploadService(QingZhouProperties qingZhouProperties) {
        return new TusFileUploadService().withStoragePath(qingZhouProperties.getFileDirectory().getTusUploadDirectory())
                .withUploadURI("/qz/api/file/upload");
    }
}

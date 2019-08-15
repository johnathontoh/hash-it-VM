package sg.com.paloit.hashit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("META-INF/MANIFEST.MF")
public class ManifestInfo implements InfoContributor {

    @Value("${Implementation-Title:notfound}")
    String title;

    @Value("${Implementation-Version:notfound}")
    String version;

    @Override
    public void contribute(final Info.Builder builder) {
        builder.withDetail("title", title);
        builder.withDetail("version", version);
    }
}

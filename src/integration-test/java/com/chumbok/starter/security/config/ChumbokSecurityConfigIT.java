package com.chumbok.starter.security.config;

import com.chumbok.security.properties.SecurityProperties;
import com.chumbok.security.util.EncryptionKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
public class ChumbokSecurityConfigIT {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ChumbokSecurityAutoConfig.class));

    @Test
    public void checkIfAutoConfigLoadBeansIfChumbokSecurityIsEnabled() {
        this.contextRunner.withPropertyValues(
                "com.chumbok.security.enable=true",
                "com.chumbok.security.assert-org-with=Chumbok",
                "com.chumbok.security.assert-tenant=true",
                "com.chumbok.security.assert-tenant-with=Chumbok",
                "com.chumbok.security.token-signing-public-key-path=classpath:public_key.der"
        )
                .run((context) -> {
                    assertThat(context).hasSingleBean(EncryptionKeyUtil.class);
                    assertThat(context).hasSingleBean(SecurityProperties.class);
                    assertThat(context.getBean(SecurityProperties.class).isEnable()).isEqualTo(true);
                    assertThat(context.getBean(SecurityProperties.class).getAssertOrgWith()).isEqualTo("Chumbok");
                    assertThat(context.getBean(SecurityProperties.class).isAssertTenant()).isEqualTo(true);
                    assertThat(context.getBean(SecurityProperties.class).getAssertTenantWith()).isEqualTo("Chumbok");
                    assertThat(context.getBean(SecurityProperties.class).getTokenSigningPublicKeyPath())
                            .isEqualTo("classpath:public_key.der");
                });
    }

    @Test
    public void checkIfAutoConfigDoesNotLoadBeansIfChumbokSecurityIsDisabled() {
        this.contextRunner.withPropertyValues(
                "com.chumbok.security.enable=false",
                "com.chumbok.security.assert-org-with=Chumbok",
                "com.chumbok.security.assert-tenant=true",
                "com.chumbok.security.assert-tenant-with=Chumbok",
                "com.chumbok.security.token-signing-public-key-path=classpath:public_key.der"
        )
                .run((context) -> {
                    assertThat(context).doesNotHaveBean(EncryptionKeyUtil.class);
                    assertThat(context).doesNotHaveBean(SecurityProperties.class);
                });
    }

    @Test
    public void checkIfAutoConfigDoesNotLoadBeansIfNoChumbokSecurityConfigurationAvailable() {
        this.contextRunner.run((context) -> {
            assertThat(context).doesNotHaveBean(EncryptionKeyUtil.class);
            assertThat(context).doesNotHaveBean(SecurityProperties.class);
        });
    }
}

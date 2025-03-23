package com.robottx.todoservice;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.InstancePrincipalsAuthenticationDetailsProvider;
import com.oracle.bmc.secrets.SecretsClient;
import com.oracle.bmc.secrets.model.Base64SecretBundleContentDetails;
import com.oracle.bmc.secrets.requests.GetSecretBundleByNameRequest;
import com.oracle.bmc.secrets.responses.GetSecretBundleByNameResponse;

import java.util.Base64;

public class Main {

    public static void main(String[] args) {
        InstancePrincipalsAuthenticationDetailsProvider provider = InstancePrincipalsAuthenticationDetailsProvider.builder().build();
        try (SecretsClient client = SecretsClient.builder()
                .region(Region.fromRegionId("eu-frankfurt-1"))
                .build(provider)) {
            GetSecretBundleByNameRequest secretBundleRequest = GetSecretBundleByNameRequest.builder()
                    .vaultId("ocid1.vault.oc1.eu-frankfurt-1.ent55a3eaahwa.abtheljsivjkf5tdizfztl454luicpvqwxhvajecx5lyih4pj76xfqf4w2ca")
                    .secretName("LocalDbUsername")
                    .build();
            GetSecretBundleByNameResponse secretBundle = client.getSecretBundleByName(secretBundleRequest);
            Base64SecretBundleContentDetails secretBundleContent = (Base64SecretBundleContentDetails) secretBundle.getSecretBundle().getSecretBundleContent();
            System.out.println(new String(Base64.getDecoder().decode(secretBundleContent.getContent())));
        }
    }

}

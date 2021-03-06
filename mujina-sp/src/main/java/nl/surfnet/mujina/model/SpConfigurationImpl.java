/*
 * Copyright 2012 SURFnet bv, The Netherlands
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.surfnet.mujina.model;

import java.security.KeyStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import nl.surfnet.spring.security.opensaml.util.KeyStoreUtil;

public class SpConfigurationImpl extends CommonConfigurationImpl implements SpConfiguration {

  private final static Logger log = LoggerFactory.getLogger(SpConfigurationImpl.class);

  private final String defaultIdpSSOServiceURL;
  private String idpSSOServiceURL;

  private String defaultProtocolBinding = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST";
  private String protocolBinding;

  private final String defaultAssertionConsumerServiceURL;
  private String assertionConsumerServiceURL;

  public SpConfigurationImpl(String defaultIdpSSOServiceURL, String defaultAssertionConsumerServiceURL) {
    this.defaultIdpSSOServiceURL = defaultIdpSSOServiceURL;
    this.defaultAssertionConsumerServiceURL = defaultAssertionConsumerServiceURL;
    this.protocolBinding = defaultProtocolBinding;
    reset();
  }

  @Override
  public void reset() {
    entityId = "http://mock-sp";
    setSigning(false);
    try {
      keyStore = KeyStore.getInstance("JKS");
      keyStore.load(null, keystorePassword.toCharArray());
      KeyStoreUtil.appendKeyToKeyStore(keyStore, "http://mock-sp", new ClassPathResource("idp-crt.pem").getInputStream(), new ClassPathResource("idp-key.pkcs8.der").getInputStream(), keystorePassword.toCharArray());
      privateKeyPasswords.put("http://mock-sp", keystorePassword);
      idpSSOServiceURL = defaultIdpSSOServiceURL;
      protocolBinding = defaultProtocolBinding;
      assertionConsumerServiceURL = defaultAssertionConsumerServiceURL;
    } catch (Exception e) {
      log.error("Unable to create default keystore", e);
    }
  }

  public void setSingleSignOnServiceURL(String idpSSOServiceURL) {
    this.idpSSOServiceURL = idpSSOServiceURL;
  }

  public String getSingleSignOnServiceURL() {
    return idpSSOServiceURL;
  }

  public String getProtocolBinding() {
    return protocolBinding;
  }

  public void setProtocolBinding(String protocolBinding) {
    this.protocolBinding = protocolBinding;
  }

  public String getAssertionConsumerServiceURL() {
    return assertionConsumerServiceURL;
  }

  public void setAssertionConsumerServiceURL(String assertionConsumerServiceURL) {
    this.assertionConsumerServiceURL = assertionConsumerServiceURL;
  }

}


package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ConfigRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Config;

@Service
@Transactional
public class ConfigService {

	//Managed repository
	@Autowired
	private ConfigRepository	configRepository;


	//Validator
	/*
	 * @Autowired
	 * private Validator validator;
	 */

	//Supporting services

	//Constructors
	public ConfigService() {
		super();
	}

	//Simple CRUD methods
	public Config create() {
		Config res;
		res = new Config();
		return res;
	}

	public Collection<Config> findAll() {
		final Collection<Config> res = this.configRepository.findAll();
		return res;
	}

	public Config findOne(final int configId) {
		final Config res = this.configRepository.findOne(configId);
		return res;
	}

	public Config save(final Config config) {
		Assert.notNull(config, "The config to save cannot be null.");

		final Authority a = new Authority();
		a.setAuthority(Authority.ADMIN);

		final UserAccount ua = LoginService.getPrincipal();
		Assert.isTrue(ua.getAuthorities().contains(a), "You must to be an Admin for this action");

		final Config res = this.configRepository.save(config);

		return res;
	}

}
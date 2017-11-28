package com.kbhkn.shareandwin;

import com.kbhkn.shareandwin.domain.Campaign;
import com.kbhkn.shareandwin.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ShareAndWinApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	CampaignRepository campaignRepository;

	public static void main(String[] args) {
		SpringApplication.run(ShareAndWinApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ShareAndWinApplication.class);
	}

	//CommandLineRunner --> Save the dummy data when server starting.
	@Override
	public void run(String... strings) throws Exception {
		campaignRepository.save(new Campaign("Test Kampanya-1"));
		campaignRepository.save(new Campaign("Test Kampanya-2"));
		campaignRepository.save(new Campaign("Test Kampanya-3"));
	}
}

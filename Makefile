deploy:
	sudo rm -rf function/target && cd function && mvn clean package && cd ../infra && cdk deploy
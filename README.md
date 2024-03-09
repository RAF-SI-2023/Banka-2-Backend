# Pravila
 - Koristiti camel case
 - Koristiti nazive na engleskom
 - Ne koristiti skracenice
 - Pre komitovanja izmena potrebno je pokrenuti aplikaciju i uveriti se da sve radi ispravno
 - Pre komitovanja izmena potrebano je odraditi “Reformat Code”
   - To se radi tako sto desnim klikom klikne na klasu sa kojom ste radili i odaberete “Reformat code” i odaberite sledece:
     - “Optimize imports”
     - “Rearrange entities”
     - “Cleanup code"
- Na ovom kanalu [backend-main](https://discord.com/channels/1212372631265742888/1212372631681110020) potrebno je `do ponedeljka uvece` ukratko napisati status vasih tiketa tako sto posaljete link ka vasem tiketu a ispod linka opisete status. Ovim izvestajem treba da se odgovori na sledeca pitanja:
     - Da li stizete da odratite i testirate ispravnost odradjenih tiketa koji su vam dodeljeni `do cetvrtka u 12:00h`
-------
 ## Sta je potrebno instalirati
 - Instalirati Docker Desktop ( link: [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/) )
 - Pokrenuti Docker Desktop
 - Otvoriti terminal i skinuti Docker image-e sledecim komandama
   - docker pull postgres
   - docker pull mongo
   - docker pull rabbitmq:3-management
     
## Sta je potrebno za uspesno pokretanje servisa
-  Otvoriti terminal i pokrenuti Docker image-e  sledecim komandama
   - docker run --name postgresDB -e POSTGRES_DB=postgresDB -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d postgres
   - docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
   - docker run --name mongoDB -d -p 27017:27017 mongo

## Dodatne napomene
Docker image-i mogu da se pokrenu i iz Docker Desktop aplikacije tako sto odete na tab `Images` or `Containtars` i u koloni `Actions` ih pokrenete 





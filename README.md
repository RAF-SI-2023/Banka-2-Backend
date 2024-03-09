# Pravila
 - Koristiti camelCase
 - Koristiti nazive na engleskom
 - Ne koristiti skracenice
 - Za datum i vreme koristiti epoch time,
 - Pre komitovanja izmena potrebno je pokrenuti aplikaciju i uveriti se da sve radi ispravno
 - Pre komitovanja izmena potrebano je odraditi “Reformat Code”
   - U InteliJ to se radi tako sto desnim klikom klikne na klasu sa kojom ste radili i odaberete “Reformat code” i odaberite sledece:
     - “Optimize imports”
     - “Rearrange entities”
     - “Cleanup code"
   - u VScode-u komandom Alt + Shift + F, odaberete "format on save" checkbox u settings-u, i sacuvate
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

---

## Workflow - pogledati [vezbe2](https://learning.raf.edu.rs/mod/url/view.php?id=22203), [vezbe3](https://video.raf.edu.rs/stream.php?video=pLpaQR%2FYY%2FWtiUv0Wc%2BZqKKg9Y%2Ff%2BCPUns7Ny4LL0AdM7dVrVj2fLMkGJxz5sNiuXpS0FLZl8q1XXF7y2eP5irdsubKDUtAXvb9u66UvYVI14l%2FiP%2Bo3QrOCY31RZYeTKR8l0XIVN61xb0NPuoreEDuizA0Od4XXRXwx1Gv8uDmEooaQZrKrunRG9CSHdgY3&file=video.mp4), [Github prezentaciju](https://docs.google.com/presentation/d/1ehKYiWcBT7fCFnmboQ1N0RgnHzhgfs6xhIym-Ss3v-w/edit#slide=id.p)
- Potrebno je forkovati (fork) `dev` granu glavnog projekata u vase lokalne repozitorijume i da radite na svom forku
- Kada hocete da odradite merge sa glavnim projektom potrebno je da kreirate `Pull request` sa vaseg forka i da izaberete da hocete da mergujete sa `dev` granom glavnog projeta
- Kada hocete da dovucete izmene sa remote repozitorijuma (`dev` grana glavnog projekta) u vase lokalne repozitorijume treba da odradite komandu `git pull upstream` gde `upstrem` redstavlja referencu na remote repozitorijum (`dev` grana glavnog projekta)




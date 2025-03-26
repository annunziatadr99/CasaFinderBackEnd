LINK di collegamento alla repository di CASAFINDER-FRONTEND: https://github.com/annunziatadr99/casafinder-frontend.git

Introduzione progetto Casa Finder Back-end

La motivazione principale per la realizzazione di questo progetto è creare un sistema completo per la gestione delle proprietà che sia sicuro, 
scalabile e facile da utilizzare. La necessità di un'applicazione come "CasaFinder" nasce dall'esigenza di avere una piattaforma centralizzata dove gli utenti 
possano trovare e pubblicare annunci di proprietà in modo semplice e sicuro. Questo progetto offre l'opportunità di applicare le conoscenze 
in ambito di sviluppo back-end, sicurezza e integrazione con sistemi front-end, fornendo una soluzione pratica e funzionale per la gestione delle proprietà immobiliari.

1. Tecnologie utilizzate
   
Questo progetto è stato sviluppato utilizzando il linguaggio di programmazione Java, grazie alla sua robustezza e ampia adozione nello sviluppo
di applicazioni enterprise. Come database relazionale è stato scelto PostgreSQL, per la sua affidabilità e supporto alle funzionalità avanzate. 
Il codice è stato scritto utilizzando l'ambiente di sviluppo integrato (IDE) IntelliJ IDEA, noto per la sua efficienza e gli strumenti avanzati di supporto allo sviluppo.

2. Struttura
   
Questo progetto implementa un sistema di gestione delle proprietà utilizzando Spring Boot. Gli utenti possono registrarsi, 
effettuare il login e creare annunci di proprietà. L'autenticazione e l'autorizzazione sono gestite tramite JWT (JSON Web Token). 
Il progetto include vari componenti come la configurazione di sicurezza, la gestione degli utenti, la gestione delle proprietà, i servizi, i repository e i payload.

3. Dipendenze Utilizzate
   
Il progetto utilizza diverse dipendenze per garantire il corretto funzionamento. Ecco un elenco delle principali dipendenze:

Spring Boot Starter Web: Per sviluppare applicazioni web RESTful.
Spring Boot Starter Data JPA: Per l'interazione con il database utilizzando JPA (Java Persistence API).
Spring Boot Starter Security: Per implementare la sicurezza dell'applicazione.
Spring Boot Starter Validation: Per la validazione delle richieste.
Spring Boot Starter DevTools: Per migliorare la produttività durante lo sviluppo.
PostgreSQL: Utilizzato come database relazionale per il progetto.
Spring Boot Starter Test: Per scrivere e eseguire test.
Cloudinary: Per la gestione del caricamento e della memorizzazione delle immagini.
Spring Boot Starter Mail: Per l'invio e gestione delle email.

4. Suddivisione del Progetto
Il progetto è suddiviso in diversi package per organizzare il codice in modo chiaro e mantenibile. Ecco la struttura del progetto:

-Controller
-Config
-Model
-Payload:
        -Request
        -Response
-Repository
-Security:
         -Jwt
         -Services
-Service

5. Ordine di Creazione dei Package e Classi
Per creare il progetto in modo ordinato, ecco l'ordine in cui i package e le classi dovrebbero essere creati:

Package: Config
classe: AppConfig(gestione del file env.properties) 
classe: CorsConfig(configurazione CORS per collegare il frontend)
classe: CloudinaryConfig(configurazione per credenziali Cloudinary)
AppConfig è l’unica configurazione che si crea all’inizio dell’impostazione del progetto, per rendere leggibile il file env.properties da Spring,
le altre due classi di configurazione non vanno create all’inizio del progetto.

Package: Model
classe: User (definisce l'entità utente)
classe: Property (definisce l'entità proprietà)
classe: Favorite (definisce l'entità dei favoriti)
classe: EmailLog (definisce l'entità per registrare i log delle email)

Package: Repository
classe: UserRepository (interfaccia per l'accesso ai dati degli utenti)
classe: PropertyRepository (interfaccia per l'accesso ai dati delle proprietà)
classe: FavoriteRepository (interfaccia per l'accesso ai dati dei favoriti)
classe: EmailLogRepository (interfaccia per l'accesso ai log delle email)

Package: Exception
classe: ResourceNotFoundException(gestisce i casi in cui una risorsa non viene trovata)
classe: BadRequestException(gestisce le richieste non valide)
classe: GlobalExceptionHandler(gestisce in modo centralizzato tutte le eccezioni)

Package: Service
classe: UserService (servizio per la gestione degli utenti)
classe: PropertyService (servizio per la gestione delle proprietà)
classe: FavoriteService (servizio per la gestione dei favoriti)
classe: EmailService (servizio per gestire l'upload delle immagini)
classe: CloudinaryService (servizio per gestire l'upload delle immagini)

Package: Security
classe: WebSecurityConfig (configurazione della sicurezza)

Package: Security.Jwt
classe: JwtUtils (utilità per la gestione dei token JWT)
classe: AuthEntryPointJwt (gestione delle eccezioni di autenticazione)
classe: AuthTokenFilter (filtro di autenticazione JWT)

Package: Security.Services
classe: UserDetailsServiceImpl (servizio per il caricamento dei dettagli dell'utente)
classe: UserDetailsImpl (implementazione dei dettagli dell'utente)

Package: Payload.Request
classe: LoginRequest (payload per la richiesta di login)
classe: RegisterRequest (payload per la richiesta di registrazione)
classe: EmailRequest (payload per l'invio delle email)

Package: Payload.Response
classe: JwtResponse (payload per la risposta del token JWT)
classe: MessageResponse (payload per la risposta dei messaggi)
classe: UserResponse (payload per i dettagli utente)

Package: Controller
classe: UserController (controller per la gestione degli utenti)
classe: PropertyController (controller per la gestione delle proprietà)
classe: FavoriteController (controller per la gestione dei favoriti)
classe: EmailController (controller per la gestione delle email)
    
6. Tipo di Sicurezza Applicata
   
La sicurezza dell'applicazione è gestita tramite JWT (JSON Web Token). Questo approccio consente di autenticare gli utenti e proteggere le endpoint dell'applicazione. 
Ogni volta che un utente effettua il login, viene generato un token JWT che viene utilizzato per autenticare le richieste successive. 
Il token viene validato da un filtro di sicurezza (AuthTokenFilter) che estende OncePerRequestFilter.

7. Scopo del Progetto Back-end
   
Lo scopo principale del progetto back-end è fornire un'API RESTful sicura e robusta per la gestione delle proprietà. Il sistema consente agli utenti di registrarsi,
effettuare il login e creare annunci di proprietà. Ogni proprietà è associata a un utente e può includere dettagli come titolo, prezzo, tipo, indirizzo e descrizione.

8. Collegamento con il Progetto "CasaFinder" di Front-end

Il progetto back-end fornisce le API necessarie per il progetto front-end "CasaFinder". Il front-end invia richieste HTTP al back-end per registrare utenti, 
effettuare il login, visualizzare le proprietà e creare nuovi annunci di proprietà. Il back-end risponde con i dati richiesti in formato JSON. 
Il token JWT ottenuto durante il login viene utilizzato per autenticare le richieste successive dal front-end. Inoltre, 
grazie alla configurazione CORS, è garantito un collegamento sicuro tra backend e frontend.

9. Logica dell'Applicazione
    
La logica dell'applicazione è basata su un'architettura a tre livelli: il livello di presentazione (controller), 
il livello di servizio (servizi) e il livello di accesso ai dati (repository).

Livello di Presentazione (Controller): I controller gestiscono le richieste HTTP e invocano i servizi appropriati per elaborare le richieste. 
Ad esempio, UserController gestisce le operazioni di registrazione e login, mentre PropertyController gestisce le operazioni relative alle proprietà.
Livello di Servizio (Servizi): I servizi contengono la logica di business e utilizzano i repository per accedere ai dati.
Ad esempio, UserService gestisce la registrazione e il caricamento dei dettagli dell'utente, mentre PropertyService gestisce la creazione e la visualizzazione delle proprietà.
Livello di Accesso ai Dati (Repository): I repository estendono JpaRepository e forniscono metodi per interagire con il database.
Ad esempio, UserRepository fornisce metodi per trovare un utente per username o e-mail, mentre PropertyRepository fornisce metodi per trovare proprietà per utente o per criteri specifici.


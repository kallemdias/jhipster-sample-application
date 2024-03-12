import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Pigeon e2e test', () => {
  const pigeonPageUrl = '/pigeon';
  const pigeonPageUrlPattern = new RegExp('/pigeon(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const pigeonSample = { ringNumber: 'holder', breeder: 'cleverly till' };

  let pigeon;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pigeons+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pigeons').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pigeons/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (pigeon) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pigeons/${pigeon.id}`,
      }).then(() => {
        pigeon = undefined;
      });
    }
  });

  it('Pigeons menu should load Pigeons page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pigeon');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Pigeon').should('exist');
    cy.url().should('match', pigeonPageUrlPattern);
  });

  describe('Pigeon page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pigeonPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Pigeon page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pigeon/new$'));
        cy.getEntityCreateUpdateHeading('Pigeon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pigeonPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pigeons',
          body: pigeonSample,
        }).then(({ body }) => {
          pigeon = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pigeons+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pigeons?page=0&size=20>; rel="last",<http://localhost/api/pigeons?page=0&size=20>; rel="first"',
              },
              body: [pigeon],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pigeonPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Pigeon page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pigeon');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pigeonPageUrlPattern);
      });

      it('edit button click should load edit Pigeon page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pigeon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pigeonPageUrlPattern);
      });

      it('edit button click should load edit Pigeon page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pigeon');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pigeonPageUrlPattern);
      });

      it('last delete button click should delete instance of Pigeon', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pigeon').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', pigeonPageUrlPattern);

        pigeon = undefined;
      });
    });
  });

  describe('new Pigeon page', () => {
    beforeEach(() => {
      cy.visit(`${pigeonPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Pigeon');
    });

    it('should create an instance of Pigeon', () => {
      cy.get(`[data-cy="ringNumber"]`).type('knitting apropos');
      cy.get(`[data-cy="ringNumber"]`).should('have.value', 'knitting apropos');

      cy.get(`[data-cy="name"]`).type('minimum');
      cy.get(`[data-cy="name"]`).should('have.value', 'minimum');

      cy.get(`[data-cy="breeder"]`).type('babysit cleverly');
      cy.get(`[data-cy="breeder"]`).should('have.value', 'babysit cleverly');

      cy.get(`[data-cy="gender"]`).select('MALE');

      cy.get(`[data-cy="birthYear"]`).type('31549');
      cy.get(`[data-cy="birthYear"]`).should('have.value', '31549');

      cy.get(`[data-cy="colorPattern"]`).select('PIED');

      cy.get(`[data-cy="longDescription"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="longDescription"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="mediumDescription"]`).type('green septicaemia');
      cy.get(`[data-cy="mediumDescription"]`).should('have.value', 'green septicaemia');

      cy.get(`[data-cy="shortDescription"]`).type('instead where questionably');
      cy.get(`[data-cy="shortDescription"]`).should('have.value', 'instead where questionably');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        pigeon = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', pigeonPageUrlPattern);
    });
  });
});

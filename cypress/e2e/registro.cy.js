describe('Flujo E2E - registro y recuperación', () => {
  it('registra un usuario correctamente y luego solicita recuperación', () => {
    const correo = `harold.${Date.now()}@test.com`;

    cy.visit('/registro');

    cy.get('[data-cy="nombre"]').type('Harold Zapata');
    cy.get('[data-cy="correo"]').type(correo);
    cy.get('[data-cy="password"]').type('Password123!');
    cy.get('[data-cy="confirmar-password"]').type('Password123!');
    cy.get('[data-cy="registrar"]').click();

    cy.get('[data-cy="mensaje-exito"]')
      .should('be.visible')
      .and('contain.text', 'Usuario registrado correctamente');

    cy.visit('/recuperar');
    cy.get('[data-cy="correo-recuperacion"]').type(correo);
    cy.get('[data-cy="recuperar"]').click();

    cy.get('[data-cy="mensaje-recuperacion"]')
      .should('be.visible')
      .and('contain.text', 'se enviaron las instrucciones de recuperación');
  });

  it('muestra validación cuando las contraseñas no coinciden', () => {
    const correo = `invalido.${Date.now()}@test.com`;

    cy.visit('/registro');
    cy.get('[data-cy="nombre"]').type('Usuario Prueba');
    cy.get('[data-cy="correo"]').type(correo);
    cy.get('[data-cy="password"]').type('Password123!');
    cy.get('[data-cy="confirmar-password"]').type('OtraPassword123!');
    cy.get('[data-cy="registrar"]').click();

    cy.get('[data-cy="error-confirmar"]')
      .should('contain.text', 'Las contraseñas no coinciden');
  });
});

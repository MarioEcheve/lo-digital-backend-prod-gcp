import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { DependenciaService } from 'app/entities/dependencia/dependencia.service';
import { IDependencia, Dependencia } from 'app/shared/model/dependencia.model';

describe('Service Tests', () => {
  describe('Dependencia Service', () => {
    let injector: TestBed;
    let service: DependenciaService;
    let httpMock: HttpTestingController;
    let elemDefault: IDependencia;
    let expectedResult: IDependencia | IDependencia[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(DependenciaService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Dependencia(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaModificacion: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Dependencia', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaModificacion: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaModificacion: currentDate,
          },
          returnedFromService
        );

        service.create(new Dependencia()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Dependencia', () => {
        const returnedFromService = Object.assign(
          {
            nombre: 'BBBBBB',
            direccion: 'BBBBBB',
            descripcion: 'BBBBBB',
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaModificacion: currentDate.format(DATE_TIME_FORMAT),
            nombreContactoComercial: 'BBBBBB',
            cargoContactoComercial: 'BBBBBB',
            telefonoPrincipalContactoComercial: 'BBBBBB',
            telefonoSecundarioContactoComercial: 'BBBBBB',
            emailContactoComercial: 'BBBBBB',
            nombreContactoTecnico: 'BBBBBB',
            cargoContactoTecnico: 'BBBBBB',
            telefonoPrincipalContactoTecnico: 'BBBBBB',
            telefonoSecundarioContactoTecnico: 'BBBBBB',
            emailContactoTecnico: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaModificacion: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Dependencia', () => {
        const returnedFromService = Object.assign(
          {
            nombre: 'BBBBBB',
            direccion: 'BBBBBB',
            descripcion: 'BBBBBB',
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaModificacion: currentDate.format(DATE_TIME_FORMAT),
            nombreContactoComercial: 'BBBBBB',
            cargoContactoComercial: 'BBBBBB',
            telefonoPrincipalContactoComercial: 'BBBBBB',
            telefonoSecundarioContactoComercial: 'BBBBBB',
            emailContactoComercial: 'BBBBBB',
            nombreContactoTecnico: 'BBBBBB',
            cargoContactoTecnico: 'BBBBBB',
            telefonoPrincipalContactoTecnico: 'BBBBBB',
            telefonoSecundarioContactoTecnico: 'BBBBBB',
            emailContactoTecnico: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaModificacion: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Dependencia', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITipoLibro } from 'app/shared/model/tipo-libro.model';

type EntityResponseType = HttpResponse<ITipoLibro>;
type EntityArrayResponseType = HttpResponse<ITipoLibro[]>;

@Injectable({ providedIn: 'root' })
export class TipoLibroService {
  public resourceUrl = SERVER_API_URL + 'api/tipo-libros';

  constructor(protected http: HttpClient) {}

  create(tipoLibro: ITipoLibro): Observable<EntityResponseType> {
    return this.http.post<ITipoLibro>(this.resourceUrl, tipoLibro, { observe: 'response' });
  }

  update(tipoLibro: ITipoLibro): Observable<EntityResponseType> {
    return this.http.put<ITipoLibro>(this.resourceUrl, tipoLibro, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITipoLibro>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITipoLibro[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

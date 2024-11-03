import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class AuthorService {
  private baseUrl = "http://localhost:8080/books-api";

  constructor(private http: HttpClient) {}

  getAuthor(id: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/authors/${id}`);
  }
}

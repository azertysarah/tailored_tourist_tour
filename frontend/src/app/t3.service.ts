import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class T3Service {

  private apiUrl : string = "http://localhost:8080/";
  
  constructor(private http : HttpClient) { }
  
  getAllPeriods() {
    return this.http.get(this.apiUrl + "periods");
  }

  getAllCommunes() {
    return this.http.get(this.apiUrl + "communes");
  }

  postSearch(data: any) {
    return this.http.post(this.apiUrl + 'tours', data);
  }
  
}

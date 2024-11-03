import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { AuthorService } from "./author.service";
import { Subscription } from "rxjs";
import { NgIf } from "@angular/common";

@Component({
  selector: "app-author-detail",
  templateUrl: "./author-detail.component.html",
  styleUrls: ["./author-detail.component.css"],
  standalone: true,
  imports: [NgIf],
})
export class AuthorDetailComponent implements OnInit, OnDestroy {
  selectedAuthor: any = null;
  private subscription!: Subscription;

  constructor(
    private route: ActivatedRoute,
    private authorService: AuthorService,
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      const id = params["id"];
      this.subscription = this.authorService.getAuthor(id).subscribe({
        next: (data: any) => {
          this.selectedAuthor = data;
        },
        error: (_: any) => {
          this.selectedAuthor = null;
        },
      });
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}

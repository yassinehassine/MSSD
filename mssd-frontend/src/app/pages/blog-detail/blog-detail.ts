import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BlogService, BlogPost } from '../../services/blog.service';

@Component({
  selector: 'app-blog-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './blog-detail.html',
  styleUrl: './blog-detail.scss'
})
export class BlogDetail implements OnInit {
  blog: BlogPost | null = null;
  loading = true;
  error = '';
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private blogService: BlogService
  ) {}
  
  ngOnInit() {
    this.route.params.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.loadBlog(+id);
      }
    });
  }
  
  loadBlog(id: number) {
    this.loading = true;
    this.error = '';
    
    this.blogService.getBlogById(id).subscribe({
      next: (blog) => {
        this.blog = blog;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Article non trouvé.';
        this.loading = false;
        console.error('Error loading blog:', err);
      }
    });
  }
  
  goBack() {
    this.router.navigate(['/blog']);
  }
  
  getImageUrl(blog: BlogPost): string {
    if (blog.imageUrl) {
      // If URL starts with http/https, use as-is
      if (blog.imageUrl.startsWith('http')) {
        return blog.imageUrl;
      }
      // If URL starts with uploads/, use the file API
      if (blog.imageUrl.startsWith('uploads/')) {
        const filename = blog.imageUrl.replace('uploads/', '');
        return `http://localhost:8080/api/files/${filename}`;
      }
      // Otherwise, assume it's just a filename
      return `http://localhost:8080/api/files/${blog.imageUrl}`;
    }
    return 'assets/img/blog/blog-default.jpg';
  }
  
  formatDate(dateString: string): string {
    return this.blogService.formatDate(dateString);
  }
  
  openYouTubeVideo(youtubeUrl: string) {
    const videoId = this.extractYouTubeId(youtubeUrl);
    if (videoId) {
      const fullUrl = `https://www.youtube.com/watch?v=${videoId}`;
      window.open(fullUrl, '_blank');
    } else {
      window.open(youtubeUrl, '_blank');
    }
  }
  
  private extractYouTubeId(url: string): string | null {
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    const match = url.match(regExp);
    return (match && match[2].length === 11) ? match[2] : null;
  }

  getCurrentUrl(): string {
    return window.location.href;
  }

  encodeUrl(url: string): string {
    return encodeURIComponent(url);
  }

  encodeText(text: string): string {
    return encodeURIComponent(text);
  }
}
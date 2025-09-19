package com.mssd.service.impl;

import com.mssd.dto.BlogDto;
import com.mssd.dto.BlogRequestDto;
import com.mssd.mapper.BlogMapper;
import com.mssd.model.Blog;
import com.mssd.repository.BlogRepository;
import com.mssd.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BlogDto> getPublishedBlogs() {
        log.debug("Getting all published blogs");
        return blogRepository.findByPublishedTrueAndActiveTrueOrderByPublishedAtDesc()
                .stream()
                .map(blogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogDto> getPublishedBlogs(Pageable pageable) {
        log.debug("Getting published blogs with pagination: {}", pageable);
        return blogRepository.findByPublishedTrueAndActiveTrueOrderByPublishedAtDesc(pageable)
                .map(blogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BlogDto getPublishedBlogBySlug(String slug) {
        log.debug("Getting published blog by slug: {}", slug);
        return blogRepository.findBySlugAndPublishedTrueAndActiveTrue(slug)
                .map(blogMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Published blog not found with slug: " + slug));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogDto> searchPublishedBlogs(String keyword, Pageable pageable) {
        log.debug("Searching published blogs with keyword: {} and pagination: {}", keyword, pageable);
        return blogRepository.searchPublishedBlogs(keyword, pageable)
                .map(blogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogDto> getPublishedBlogsByCategory(String category, Pageable pageable) {
        log.debug("Getting published blogs by category: {} with pagination: {}", category, pageable);
        return blogRepository.findByPublishedTrueAndActiveTrueAndCategoryOrderByPublishedAtDesc(category, pageable)
                .map(blogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogDto> getAllBlogs() {
        log.debug("Getting all blogs for admin");
        return blogRepository.findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(blogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogDto> getAllBlogs(Pageable pageable) {
        log.debug("Getting all blogs for admin with pagination: {}", pageable);
        return blogRepository.findByActiveTrueOrderByCreatedAtDesc(pageable)
                .map(blogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BlogDto getBlogById(Long id) {
        log.debug("Getting blog by id: {}", id);
        return blogRepository.findById(id)
                .filter(blog -> blog.getActive())
                .map(blogMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public BlogDto getBlogBySlug(String slug) {
        log.debug("Getting blog by slug: {}", slug);
        return blogRepository.findBySlugAndActiveTrue(slug)
                .map(blogMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Blog not found with slug: " + slug));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogDto> searchAllBlogs(String keyword, Pageable pageable) {
        log.debug("Searching all blogs with keyword: {} and pagination: {}", keyword, pageable);
        return blogRepository.searchAllBlogs(keyword, pageable)
                .map(blogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogDto> getBlogsByCategory(String category, Pageable pageable) {
        log.debug("Getting blogs by category: {} with pagination: {}", category, pageable);
        return blogRepository.findByActiveTrueAndCategoryOrderByCreatedAtDesc(category, pageable)
                .map(blogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogDto> getBlogsByAuthor(String author, Pageable pageable) {
        log.debug("Getting blogs by author: {} with pagination: {}", author, pageable);
        return blogRepository.findByActiveTrueAndAuthorContainingIgnoreCaseOrderByCreatedAtDesc(author, pageable)
                .map(blogMapper::toDto);
    }

    @Override
    public BlogDto createBlog(BlogRequestDto blogRequestDto) {
        log.debug("Creating new blog with title: {}", blogRequestDto.getTitle());
        
        Blog blog = blogMapper.toEntity(blogRequestDto);
        
        // Ensure slug uniqueness
        String originalSlug = blog.getSlug();
        String uniqueSlug = ensureUniqueSlug(originalSlug, null);
        blog.setSlug(uniqueSlug);
        
        Blog savedBlog = blogRepository.save(blog);
        log.info("Created blog with id: {} and slug: {}", savedBlog.getId(), savedBlog.getSlug());
        
        return blogMapper.toDto(savedBlog);
    }

    @Override
    public BlogDto updateBlog(Long id, BlogRequestDto blogRequestDto) {
        log.debug("Updating blog with id: {}", id);
        
        Blog existingBlog = blogRepository.findById(id)
                .filter(blog -> blog.getActive())
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        
        blogMapper.updateEntity(existingBlog, blogRequestDto);
        
        // Ensure slug uniqueness if slug changed
        String newSlug = existingBlog.getSlug();
        String uniqueSlug = ensureUniqueSlug(newSlug, id);
        existingBlog.setSlug(uniqueSlug);
        
        Blog savedBlog = blogRepository.save(existingBlog);
        log.info("Updated blog with id: {} and slug: {}", savedBlog.getId(), savedBlog.getSlug());
        
        return blogMapper.toDto(savedBlog);
    }

    @Override
    public void deleteBlog(Long id) {
        log.debug("Deleting blog with id: {}", id);
        
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        
        blogRepository.delete(blog);
        log.info("Deleted blog with id: {}", id);
    }

    @Override
    public void deactivateBlog(Long id) {
        log.debug("Deactivating blog with id: {}", id);
        
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        
        blog.setActive(false);
        blog.setPublished(false); // Also unpublish when deactivating
        blogRepository.save(blog);
        log.info("Deactivated blog with id: {}", id);
    }

    @Override
    public void activateBlog(Long id) {
        log.debug("Activating blog with id: {}", id);
        
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        
        blog.setActive(true);
        blogRepository.save(blog);
        log.info("Activated blog with id: {}", id);
    }

    @Override
    public BlogDto publishBlog(Long id) {
        log.debug("Publishing blog with id: {}", id);
        
        Blog blog = blogRepository.findById(id)
                .filter(b -> b.getActive())
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        
        blog.setPublished(true);
        if (blog.getPublishedAt() == null) {
            blog.setPublishedAt(LocalDateTime.now());
        }
        
        Blog savedBlog = blogRepository.save(blog);
        log.info("Published blog with id: {}", id);
        
        return blogMapper.toDto(savedBlog);
    }

    @Override
    public BlogDto unpublishBlog(Long id) {
        log.debug("Unpublishing blog with id: {}", id);
        
        Blog blog = blogRepository.findById(id)
                .filter(b -> b.getActive())
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
        
        blog.setPublished(false);
        
        Blog savedBlog = blogRepository.save(blog);
        log.info("Unpublished blog with id: {}", id);
        
        return blogMapper.toDto(savedBlog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCategories() {
        log.debug("Getting all blog categories");
        return blogRepository.findDistinctCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAuthors() {
        log.debug("Getting all blog authors");
        return blogRepository.findDistinctAuthors();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSlugAvailable(String slug) {
        return !blogRepository.existsBySlugAndActiveTrue(slug);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSlugAvailable(String slug, Long excludeId) {
        return !blogRepository.existsBySlugAndActiveTrueAndIdNot(slug, excludeId);
    }

    private String ensureUniqueSlug(String baseSlug, Long excludeId) {
        String uniqueSlug = baseSlug;
        int counter = 1;
        
        while (excludeId != null ? 
               !isSlugAvailable(uniqueSlug, excludeId) : 
               !isSlugAvailable(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        }
        
        return uniqueSlug;
    }
}
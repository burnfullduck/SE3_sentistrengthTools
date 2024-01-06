# -*- coding = utf-8 -*-
# @Time : 2023/5/17 19:45
# @Author : 詹泽淇
# @File : SpiderAPI.py
# @Software : PyCharm
import requests
from lxml import etree
import json
import os
import time


# 解析评论
def my_parse(element, issue_no, sub_name, text_no):
    author = element.xpath(".//a[starts-with(@class,'author')]/text()")[0]
    raw_time = element.xpath(".//relative-time/@datetime")[0]
    our_time = (str(raw_time))[0: 10]
    # description_list = element.xpath(".//td[contains(@class, 'comment-body')]/p/text()")
    # description = ""
    # for p in description_list:
    #     description += p
    # 解析文本
    description_list = element.xpath(".//td[contains(@class, 'comment-body')]//*[(name()='p' or name()='strong' or "
                                     "name()='li' or name() = 'h1' or name()='h2' or name()='h3' or name()='h4' or "
                                     "name()='h5' or name()='h6') and not(ancestor::blockquote) ]/text()")
    description = ""
    for p in description_list:
        description += p
    if sub_name is None:
        sub_name = author
    return {
        'IssueNo': issue_no,
        'TextNo': issue_no + '-' + str(text_no),
        'SubName': sub_name,
        'Reviewer': author,
        'Time': our_time,
        'Text': description,
        'Senti': 9
    }


# 解析并获取问题
def parse_issue(element, issue_no, sub_name, text_no):
    author = element.xpath(".//a[starts-with(@class,'author')]/text()")[0]
    raw_time = element.xpath(".//relative-time/@datetime")[0]
    our_time = (str(raw_time))[0: 10]
    # 解析文本
    description = element.xpath(".//bdi[starts-with(@class,'js-issue')]/text()")[0]
    if sub_name is None:
        sub_name = author
    return {
        'IssueNo': issue_no,
        'TextNo': issue_no + '-' + str(text_no),
        'SubName': sub_name,
        'Reviewer': author,
        'Time': our_time,
        'Text': description,
        'Senti': 9
    }


# 获得每个问题的详细页面的url
def get_issue_list():
    # 项目url，以后可以动态输入
    url = 'https://github.com/apache/superset/issues'
    headers = {
        'User-Agent': "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
                      "Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.42 "
    }
    parser = etree.HTMLParser(encoding='utf-8')
    issue_list = []
    # filename = './ip%d.html'
    # 设置爬取1到4页的问题，以后可以动态输入
    for num in range(1, 46):
        params = {
            'page': num,
            'q': 'is:issue is:close'
        }
        # 循环是为了请求失败后不断重试直到成功
        while True:
            try:
                response = requests.get(url=url, headers=headers, params=params, proxies={"https": "127.0.0.1:7892"})
                page_text = response.text
                # new_filename = filename % num
                # print(new_filename)
                # with open(new_filename, 'w', encoding='utf-8') as fp:
                #     fp.write(page_text)
                tree = etree.HTML(page_text, parser=parser)
                href_list = tree.xpath('//a[@data-hovercard-type="issue"]/@href')
                for li in href_list:
                    issue_src = "https://github.com" + li
                    issue_list.append(issue_src)
                    # response = requests.get(url=issue_src, headers=headers, proxies={"https": "192.168.3.9:7892"})
                    # new_filename = issue_src.split('/')[-1] + ".html"
                    # page_text = response.text
                    # with open(new_filename, 'w', encoding='utf-8') as fp:
                    #     fp.write(page_text)
                    # time.sleep(1)
                print("success")
                break
            except requests.exceptions.RequestException as e:
                continue
    return issue_list


# 获得每个评论的信息，包括提交序号 文本序号 提交者 评审者 时间 文本 情绪值（待生成）
def get_comment_list(issue_list):
    session = requests.Session()
    headers = {
        'User-Agent': "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
                      "Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.42 "
    }
    parser = etree.HTMLParser(encoding='utf-8')
    comments_list = []
    for issue_url in issue_list:
        while True:
            try:
                response = session.get(url=issue_url, headers=headers, proxies={"https": "127.0.0.1:7892"})
                page_detail_text = response.text
                # new_filename = issue_url.split('/')[-1] + ".html"
                # with open(new_filename, 'w', encoding='utf-8') as fp:
                #     fp.write(page_text)
                tree = etree.HTML(page_detail_text, parser=parser)
                issue_num = issue_url.split('/')[-1]

                # 解析问题：
                issue = parse_issue(tree, issue_num, None, 1)
                print(issue)
                comments_list.append(issue)
                top_list = tree.xpath('//div[@data-hpc]/div')
                # issue_tree = top_list[0]
                # issue = my_parse(issue_tree, issue_num, None, 1)
                # print(issue)
                # issue_title = tree.xpath()
                comments = top_list[1]
                comments_tree_list = comments.xpath("./div[starts-with(@class, 'js-timeline-item')]/div[@data-url]")
                i = 2
                for comment_tree in comments_tree_list:
                    comment = my_parse(comment_tree, issue_num, issue['SubName'], i)
                    i = i + 1
                    comments_list.append(comment)
                    print(comment)
                break
            except requests.exceptions.RequestException as e:
                continue

    return comments_list


if __name__ == '__main__':
    my_issue_list = get_issue_list()
    comment_list = get_comment_list(my_issue_list)

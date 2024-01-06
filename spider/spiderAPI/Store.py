# -*- coding = utf-8 -*-
# @Time : 2023/5/23 15:10
# @Author : 詹泽淇
# @File : Store.py
# @Software : PyCharm
import re
import pymysql.converters
import SpiderAPI


# 用于排除除空和非英文的文本
def text_need_to_be_filtered(text):
    remove_nota = u'[’·°–!"#$%&\'()*+,-./:;<=>?@，。?★、…【】（）《》？“”‘’！[\\]^_`{|}~ ]+'
    test_text = re.sub(remove_nota, '', text)
    if len(test_text) == 0:
        return True
    for character in test_text:
        if not character.isalpha():
            return True
    return False


# 连接数据库
db = pymysql.connect(host='124.221.102.208', port=3306, user='root_user', passwd='zxk1019@!', database='Senti_Schema',
                     charset='utf8')

# 获取游标
cur = db.cursor()

# 获取数据
issue_list = SpiderAPI.get_issue_list()
comment_list = SpiderAPI.get_comment_list(issue_list)
for comment in comment_list:
    issueNo = comment.get("IssueNo")
    TextNo = comment.get("TextNo")
    SubName = comment.get("SubName")
    Reviewer = comment.get("Reviewer")
    Time = comment.get("Time")
    # 处理数据，非英文评论，空值，不存入数据库
    Text = pymysql.converters.escape_string(comment.get("Text"))
    if text_need_to_be_filtered(Text):
        continue
    Senti = comment.get('Senti')
    # 数据操作
    cur.execute(
        f'insert ignore into superset values("{issueNo}","{TextNo}","{SubName}","{Reviewer}","{Time}","{Text}","{Senti}");')

# 将修改的内容提交到数据库
db.commit()

# 关闭游标和数据库连接
cur.close()
db.close()
